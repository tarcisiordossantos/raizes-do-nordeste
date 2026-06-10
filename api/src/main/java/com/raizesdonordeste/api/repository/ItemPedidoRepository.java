package com.raizesdonordeste.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.ItemPedido;
import java.util.List;



public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long>{
    List<ItemPedido> findByPedidoIdAndStatusProduto(Long pedidoId, String statusProduto);
}
