package com.raizesdonordeste.api.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raizesdonordeste.api.api.dto.PedidoRequestDTO;
import com.raizesdonordeste.api.api.dto.PedidoResponseDTO;
import com.raizesdonordeste.api.api.dto.PedidoUpdateDTO;
import com.raizesdonordeste.api.application.exception.CancelamentoNaoPermitidoException;
import com.raizesdonordeste.api.application.exception.FalhaNoPagamentoException;
import com.raizesdonordeste.api.domain.ItemPedido;
import com.raizesdonordeste.api.domain.Pedido;
import com.raizesdonordeste.api.domain.Produto;
import com.raizesdonordeste.api.domain.Unidade;
import com.raizesdonordeste.api.domain.Usuario;
import com.raizesdonordeste.api.domain.enuns.CanalOrigem;
import com.raizesdonordeste.api.infrastructure.gateway.PagamentoGateway;
import com.raizesdonordeste.api.infrastructure.repository.PedidoRepository;
import com.raizesdonordeste.api.infrastructure.repository.ProdutoRepository;
import com.raizesdonordeste.api.infrastructure.repository.UnidadeRepository;
import com.raizesdonordeste.api.infrastructure.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final UnidadeRepository unidadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstoquesService estoquesService;
    private final CardapioService cardapioService;
    private final PagamentoGateway pagamentoGateway;


    @Transactional
    public PedidoResponseDTO realizarPedido(PedidoRequestDTO dto){

        //1. Recupera Unidade que atende o pedido
        Unidade unidade = unidadeRepository.findById(dto.unidadeId())
            .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado unidade com ID " + dto.unidadeId()));

        //2. Recupera Usuario que realizou o pedido
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
            .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado usuário com ID " + dto.usuarioId()));

        //3. Criar lista com todos os Ids dos produtos
        List<Long> idsProdutos = dto.itensPedido().stream()
            .map(item -> item.produtoId()).toList();
        
        //4. Pegar os produtos no banco de dados
        List<Produto> produtos = produtoRepository.findAllById(idsProdutos);

        //5. Cria uma lista de ItemPedido com os produtos
        List<ItemPedido> itens = dto.itensPedido().stream()
            .map(item -> {
                Produto produto = produtos.stream()
                    .filter(p -> p.getId().equals(item.produtoId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado produto com ID " + item.produtoId()));
                return item.toEntity(produto);
            }).toList();
        
        //6. Verificar disponibilidade dos produtos no cardápio ativo da unidade
        cardapioService.consultarDisponibilidadeProdutos(unidade, itens);

        //7. Verificar disponibilidade dos produtos nos estoques (produtos e ingredientes) da unidade
        estoquesService.consultarDisponibilidadeProdutos(unidade, itens);
        
        //8. Cria a entidade Pedido para ser salva no Banco de Dados
        Pedido novoPedido = dto.toEntity(usuario, unidade, itens);

        //9. Caso usar fidelidade seja true, o desconto equivale a 5% do valor da fidelidade acumulada, limitado a 20% do valor dos produtos
        BigDecimal desconto = new BigDecimal("0");
        if(dto.usarFidelidade() != null && dto.usarFidelidade()){
            desconto = BigDecimal.valueOf(usuario.getPontosFidelidade())
                .multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalProdutos = novoPedido.calcularTotalProdutos();
            BigDecimal limiteMaximoDesconto = totalProdutos.multiply(new BigDecimal("0.20"))
                .setScale(2, RoundingMode.HALF_UP);
            if(desconto.compareTo(limiteMaximoDesconto) > 0){
                desconto = limiteMaximoDesconto;
                int pontosUtilizados = desconto.multiply(new BigDecimal("20")).intValue();
                usuario.setPontosFidelidade(usuario.getPontosFidelidade() - pontosUtilizados);
            } else {
                usuario.setPontosFidelidade(0);
            }
            novoPedido.setDesconto(desconto);
        } 

        //10. Calcular o valor total já com desconto aplicado
        novoPedido.setValorTotal(novoPedido.calcularValorTotal());

        //11. Validação do cartão
        String metodo = dto.pagamento().metodoPagamento();
        String numero = dto.pagamento().numeroCartao();
        YearMonth vencimento = dto.pagamento().vencimentoCartao();
        String cvv = dto.pagamento().cvv();
        if(!"PIX".equals(metodo) && (numero == null || numero.isBlank() || vencimento == null || cvv == null || cvv.isBlank())){
            throw new IllegalArgumentException("Para método pagamento CREDITO ou DEBITO, as informações do cartão devem ser preenchidas.");
        }

        //12. Confirmação do pagamento (mock)
        boolean confirmacaoPagamento;
        if("PIX".equals(metodo)){
            confirmacaoPagamento = pagamentoGateway.validarPagamentoPix(novoPedido);
        }else {
            confirmacaoPagamento = pagamentoGateway.validarPagamentoCartao(novoPedido, numero, vencimento, cvv);
        }

        if (!confirmacaoPagamento){
            throw new FalhaNoPagamentoException("Houve uma falha na tentativa de pagamento");
        } 
        else { // 13. Registra pagamento, muda status do pedido e gera pontos de fidelidade 
            novoPedido.getPagamentos().getFirst().registrarPagamento();
            novoPedido.setStatusPedido("PAGAMENTO_CONFIRMADO");
            int novosPontos = (novoPedido.getValorTotal().subtract(novoPedido.getValorEntrega())).intValue();
            usuario.setPontosFidelidade(usuario.getPontosFidelidade() + novosPontos);
        }

        //14. Dar baixa nos estoques da Unidade
        estoquesService.baixarEstoques(unidade, itens);

        //15. Salva novo pedido
        pedidoRepository.save(novoPedido);

        log.info("[AUDITORIA] Novo pedido criado. Codigo do Pedido {} | Unidade ID {} | Cliente ID {} | Valor Total: R$ {}",
            novoPedido.getId(), unidade.getId(), usuario.getId(), novoPedido.getValorTotal());

        return PedidoResponseDTO.fromEntity(novoPedido);
    }



    public Page<PedidoResponseDTO> consultarPedidosUnidade(Long id,  Pageable pageable){
        if(!unidadeRepository.existsById(id)){
            throw new EntityNotFoundException("Não foi encontrado unidade com ID " + id);
        }
        Page<Pedido> pedidos = pedidoRepository.findByUnidadeId(id, pageable);
        Page<PedidoResponseDTO> pedidosDTO = pedidos.map(p -> PedidoResponseDTO.fromEntity(p));

        return pedidosDTO;
    }



    public Page<PedidoResponseDTO> consultarPedidosUsuario(Long id,  Pageable pageable){
        if(!usuarioRepository.existsById(id)){
            throw new EntityNotFoundException("Não foi encontrado usuário com ID " + id);
        }
        Page<Pedido> pedidos = pedidoRepository.findByUsuarioId(id, pageable);
        Page<PedidoResponseDTO> pedidosDTO = pedidos.map(p -> PedidoResponseDTO.fromEntity(p));
        
        return pedidosDTO;
    }



    public Page<PedidoResponseDTO> consultarPorUnidadeEUsuario(Long unidadeId, Long usuarioId, Pageable pageable){
        if(!unidadeRepository.existsById(unidadeId)){
            throw new EntityNotFoundException("Não foi encontrado unidade com ID " + unidadeId);
        }
        if(!usuarioRepository.existsById(usuarioId)){
            throw new EntityNotFoundException("Não foi encontrado usuário com ID " + usuarioId);
        }
        Page<Pedido> pedidos = pedidoRepository.findByUnidadeIdAndUsuarioId(unidadeId, usuarioId, pageable);
        Page<PedidoResponseDTO> pedidosDTO = pedidos.map(p -> PedidoResponseDTO.fromEntity(p));
        
        return pedidosDTO;
    }



    public Page<PedidoResponseDTO> consultarPorUnidadeECanal(Long unidadeId, CanalOrigem canal, Pageable pageable){
        if(!unidadeRepository.existsById(unidadeId)){
            throw new EntityNotFoundException("Não foi encontrado unidade com ID " + unidadeId);
        }

        Page<Pedido> pedidos = pedidoRepository.findByUnidadeIdAndCanalOrigem(unidadeId, canal, pageable);
        Page<PedidoResponseDTO> pedidosDTO = pedidos.map(p -> PedidoResponseDTO.fromEntity(p)); 
        
        return pedidosDTO;
    }



    public Page<PedidoResponseDTO> consultarPorCanal(CanalOrigem canal, Pageable pageable){

        Page<Pedido> pedidos = pedidoRepository.findByCanalOrigem(canal, pageable);
        Page<PedidoResponseDTO> pedidosDTO = pedidos.map(p -> PedidoResponseDTO.fromEntity(p));
        
        return pedidosDTO;
    }



    @Transactional
    public PedidoResponseDTO atualizarPedido(Long pedidoId, PedidoUpdateDTO dto){
        
        Pedido pedidoAtualizavel = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado pedido com ID " + pedidoId));
        
        if(!usuarioRepository.existsById(dto.usuarioId())){
            throw new EntityNotFoundException("Não foi encontrado usuário com ID " + dto.usuarioId());
        }

        if(!pedidoAtualizavel.getUsuario().getId().equals(dto.usuarioId())){
            throw new IllegalArgumentException("O pedido não pertece ao usuário informado.");
        }

       //Procedimentos para cancelamento do pedido
        if("CANCELADO".equals(dto.statusPedido())){
            if(!"PAGAMENTO_CONFIRMADO".equals(pedidoAtualizavel.getStatusPedido()) && !"AGUARDANDO_PAGAMENTO".equals(pedidoAtualizavel.getStatusPedido())){
                throw new CancelamentoNaoPermitidoException("Cancelamento permitido apenas para pedidos com status AGUARDANDO_PAGAMENTO ou PAGAMENTO_CONFIRMADO");    
            }

            Long idUltimoPedido = pedidoRepository.findTopByUsuarioIdOrderByIdDesc(dto.usuarioId())
                .map(p -> p.getId()).orElse(0L);

            if(!pedidoAtualizavel.getId().equals(idUltimoPedido)){
                throw new CancelamentoNaoPermitidoException("Cancelamento permitido apenas para o último pedido realizado pelo usuário");
            }

            pedidoAtualizavel.setStatusPedido("CANCELADO");
            pedidoAtualizavel.setObservacoes(dto.observacoes());

            pedidoAtualizavel.getPagamentos().getLast().setStatusPagamento("A_ESTORNAR");
            pedidoAtualizavel.getPagamentos().getLast().setValor(BigDecimal.ZERO);
            pedidoAtualizavel.getPagamentos().getLast().setDataPagamento(LocalDateTime.now());

            //Procedimentos para retornar pontuação de fidelidade ao valor anterior ao pedido cancelado
            if(pedidoAtualizavel.getDesconto() != null && pedidoAtualizavel.getDesconto().compareTo(BigDecimal.ZERO) > 0){
                int pontosGastos = pedidoAtualizavel.getDesconto().multiply(new BigDecimal("20")).intValue();
                int pontosGanhos = pedidoAtualizavel.getValorTotal().subtract(pedidoAtualizavel.getValorEntrega()).intValue();
                int saldoAtualizado = pedidoAtualizavel.getUsuario().getPontosFidelidade() + pontosGastos - pontosGanhos;
                pedidoAtualizavel.getUsuario().setPontosFidelidade(Math.max(0, saldoAtualizado));
            } else {
                int pontosGanhos = pedidoAtualizavel.getValorTotal().subtract(pedidoAtualizavel.getValorEntrega()).intValue();
                int saldoAtualizado = pedidoAtualizavel.getUsuario().getPontosFidelidade() - pontosGanhos;
                pedidoAtualizavel.getUsuario().setPontosFidelidade(Math.max(0, saldoAtualizado));
            }

            log.warn("[AUDITORIA] Pedido ID {} foi CANCELADO pelo Usuario ID {}", pedidoId, dto.usuarioId());

        } else { //Caso a atualização seja apenas para adicionar uma observação ao pedido
            pedidoAtualizavel.setObservacoes(dto.observacoes());
        }

        return PedidoResponseDTO.fromEntity(pedidoAtualizavel);
    }

}