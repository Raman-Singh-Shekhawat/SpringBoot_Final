package com.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventorymanagement.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}