package com.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventorymanagement.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}