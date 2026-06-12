package com.raizesdonordeste.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.raizesdonordeste.api.domain.ItemPedido;
import com.raizesdonordeste.api.domain.Pagamento;
import com.raizesdonordeste.api.domain.Pedido;
import com.raizesdonordeste.api.domain.Unidade;
import com.raizesdonordeste.api.domain.Usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record PedidoRequestDTO(
    @Schema(example = "APP|BALCAO|TOTEN")
    @Pattern(regexp = "^(APP|BALCAO|TOTEN)?$")
    String canalOrigem,
    @Schema(example = "ENTREGA|RETIRADA")
    @Pattern(regexp = "^(ENTREGA|RETIRADA)?$")
    String formaEntrega,
    @Size(max = 255) 
    String observacoes,
    @Schema(example = "1")
    @NotNull(message = "campo de preenchimento obrigatório") 
    Long usuarioId,
    @Schema(example = "1")
    @NotNull(message = "campo de preenchimento obrigatório")  
    Long unidadeId,
    @Valid @NotEmpty List<ItemPedidoRequestDTO> itensPedido,
    @Valid @NotNull(message = "campo de preenchimento obrigatório")   
    PagamentoRequestDTO pagamento
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
