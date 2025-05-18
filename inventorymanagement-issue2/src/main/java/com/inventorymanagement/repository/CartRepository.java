package com.inventorymanagement.repository;

import com.inventorymanagement.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomerId(Long customerId);
}

//public interface CartItemRepository extends JpaRepository<CartItem, Long> {
//}

