package com.raizesdonordeste.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raizesdonordeste.api.domain.ItemPedido;
import java.util.List;


@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long>{
    List<ItemPedido> findByPedidoIdAndStatusProduto(Long pedidoId, String statusProduto);
}
