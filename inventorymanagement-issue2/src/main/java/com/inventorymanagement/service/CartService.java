package com.inventorymanagement.service;

import com.inventorymanagement.DTO.CartItemRequest;
import com.inventorymanagement.DTO.CartRequest;
import com.inventorymanagement.entity.*;
import com.inventorymanagement.repository.CartItemRepository;
import com.inventorymanagement.repository.CartRepository;
import com.inventorymanagement.repository.CustomerRepository;
import com.inventorymanagement.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;

@Service
public class CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ItemRepository itemRepository;

    public void addItemsToCart(CartRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Check if cart already exists
        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    return cartRepository.save(newCart);
                });

        for (CartItemRequest itemRequest : request.getItems()) {
            Item item = itemRepository.findById(itemRequest.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            // Check if item already exists in cart
            Optional<CartItem> existingItem = cart.getItems().stream()
                    .filter(ci -> ci.getItem().getId().equals(item.getId()))
                    .findFirst();

            if (existingItem.isPresent()) {
                CartItem ci = existingItem.get();
                ci.setQuantity(ci.getQuantity() + itemRequest.getQuantity()); // update quantity
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setItem(item);
                cartItem.setQuantity(itemRequest.getQuantity());
                cartItem.setCart(cart);
                cart.getItems().add(cartItem);
            }
        }

        cartRepository.save(cart); // Cascade saves items
    }

    public Cart getCartByCustomerId(Long customerId) {
        return cartRepository.findByCustomerId(customerId).orElse(null);
    }

    public void removeItemFromCart(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found with id: " + cartItemId));

        cartItemRepository.delete(item);
    }

}

