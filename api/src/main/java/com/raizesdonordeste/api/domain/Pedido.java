package com.raizesdonordeste.api.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.raizesdonordeste.api.domain.enuns.CanalOrigem;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pedido")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "data_pedido")
    private LocalDateTime dataPedido;
    @Column(name = "status_pedido")
    @Size(max = 50)
    private String statusPedido;
    @Column(name = "canal_origem")
    @Enumerated(EnumType.STRING)
    private CanalOrigem canalOrigem;
    @Column(name = "forma_entrega")
    @Size(max = 50)
    private String formaEntrega;
    @Column(name = "valor_entrega")
    @PositiveOrZero
    private BigDecimal valorEntrega;
    @Column(name = "prazo_estimado")
    @PositiveOrZero
    private int prazoEstimado;
    @Column
    @PositiveOrZero
    private BigDecimal desconto;
    @Column(name = "valor_total")
    @PositiveOrZero
    private BigDecimal valorTotal;
    @Column(name = "observacao")
    @Size(max = 255)
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({"pedidos","perfis","enderecos","consentimentos"})
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "unidade_id")
    @JsonIgnoreProperties({"pedidos","endereco","regiao","cardapios","estoquesProdutos","estoquesIngredientes"})
    private Unidade unidade;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemPedido> itensPedido = new ArrayList<>();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Pagamento> pagamentos = new ArrayList<>();

    public void adicionarItem(ItemPedido item) {
        itensPedido.add(item);
        item.setPedido(this);
    }

    public void removerItem(ItemPedido item) {
        itensPedido.remove(item);
        item.setPedido(null);
    }

    public BigDecimal calcularValorTotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal entrega = (this.valorEntrega != null) ? this.valorEntrega : BigDecimal.ZERO;
        BigDecimal desc = (this.desconto != null) ? this.desconto : BigDecimal.ZERO;

        for(ItemPedido item : itensPedido){
            subtotal = subtotal.add(item.calcularSubtotal()); 
        }
        this.valorTotal = subtotal.add(entrega).subtract(desc);
        return valorTotal;
    }

    public BigDecimal calcularTotalProdutos() {
        BigDecimal subtotal = BigDecimal.ZERO;

        for(ItemPedido item : itensPedido){
            subtotal = subtotal.add(item.calcularSubtotal()); 
        }

        return subtotal;
    }

    public void calcularPrazoEntrega() {
        //A implementar
    }
}
