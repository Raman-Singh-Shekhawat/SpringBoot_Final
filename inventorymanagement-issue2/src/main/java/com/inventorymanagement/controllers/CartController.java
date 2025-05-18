package com.inventorymanagement.controllers;

import com.inventorymanagement.DTO.CartRequest;
import com.inventorymanagement.entity.Cart;
import com.inventorymanagement.entity.CartItem;
import com.inventorymanagement.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{customerId}/items")
    public List<CartItem> getCartItems(@PathVariable Long customerId) {
        Cart cart = cartService.getCartByCustomerId(customerId);
        return cart != null ? cart.getItems() : List.of();  // safely return empty list if cart is null
    }


    @PostMapping("/add")
    public ResponseEntity<String> addItemsToCart(@RequestBody CartRequest request) {
        cartService.addItemsToCart(request);
        return ResponseEntity.ok("Items added to cart successfully.");
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<String> removeItemFromCart(@PathVariable Long cartItemId) {
        try {
            cartService.removeItemFromCart(cartItemId);
            return ResponseEntity.ok("Item removed from cart successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Failed to remove item: " + e.getMessage());
        }
    }


//    @GetMapping("/api/cart/{userId}")
//    public List<CartItem> getCartItems(@PathVariable("userId") Long userId) {
//        return cartService.getCartByCustomerId(userId); // Fetch the cart items based on the userId
//    }

//
//    @PostMapping("/{customerId}/add")
//    public Cart addToCart(@PathVariable Long customerId, @RequestParam Long itemId, @RequestParam int quantity) {
//        return cartService.addItemToCart(customerId, itemId, quantity);
//    }
//
//    @DeleteMapping("/{customerId}/remove")
//    public Cart removeFromCart(@PathVariable Long customerId, @RequestParam Long itemId) {
//        return cartService.removeItemFromCart(customerId, itemId);
//    }
//
//    @DeleteMapping("/{customerId}/clear")
//    public void clearCart(@PathVariable Long customerId) {
//        cartService.clearCart(customerId);
//    }
}
