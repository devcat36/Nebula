package com.albireo.nebula.repository;

import com.albireo.nebula.model.Item;
import com.albireo.nebula.model.Order;
import com.albireo.nebula.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderByOrderId(Long orderId);
    Optional<Order> findOrderByItemAndBuyer(Item item, User buyer);
    List<Order> findOrdersByItemAndItem_Seller(Item item, User seller);
    List<Order> findOrdersByBuyer(User buyer);
    List<Order> findOrdersByItem_Seller(User seller);
}