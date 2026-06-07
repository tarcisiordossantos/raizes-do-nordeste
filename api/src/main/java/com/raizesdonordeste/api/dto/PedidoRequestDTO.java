package com.raizesdonordeste.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.raizesdonordeste.api.domain.ItemPedido;
import com.raizesdonordeste.api.domain.Pagamento;
import com.raizesdonordeste.api.domain.Pedido;
import com.raizesdonordeste.api.domain.Unidade;
import com.raizesdonordeste.api.domain.Usuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PedidoRequestDTO(
    @Size(max = 50) String canalOrigem,
    @Size(max = 50) String formaEntrega,
    @Size(max = 255) String observacoes,
    @NotNull Long usuarioId,
    @NotNull Long unidadeId,
    @Valid @NotEmpty List<ItemPedidoRequestDTO> itensPedido,
    @NotNull PagamentoRequestDTO pagamento
) {
    public Pedido toEntity(Usuario usuario, Unidade unidade, List<ItemPedido> itens){
        Pedido pedido = new Pedido();
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatusPedido("AGUARDANDO_PAGAMENTO");
        pedido.setCanalOrigem(this.canalOrigem);
        pedido.setFormaEntrega(this.formaEntrega);
        pedido.setObservacoes(this.observacoes);
        pedido.setUsuario(usuario);
        pedido.setUnidade(unidade);
        pedido.setItensPedido(itens);
        for(ItemPedido item : itens){
            item.setPedido(pedido);
        }
        pedido.setValorTotal(pedido.calcularValorTotal());

        Pagamento pagamentoEntity = this.pagamento.toEntity();
        pagamentoEntity.setPedido(pedido);
        pedido.getPagamentos().add(pagamentoEntity);

        return pedido;
    }
}
