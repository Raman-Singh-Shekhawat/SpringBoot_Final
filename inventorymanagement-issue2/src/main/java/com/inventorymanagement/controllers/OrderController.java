package com.inventorymanagement.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventorymanagement.DTO.OrderDTO;
import com.inventorymanagement.entity.Order;
import com.inventorymanagement.entity.OrderStatus;
import com.inventorymanagement.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<Map<String, String>> createOrder(@RequestBody Order order) {
		orderService.createOrder(order);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Order created successfully!");
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
		Optional<OrderDTO> orderDtoOptional = orderService.findOrderById(id);

		return orderDtoOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<OrderDTO>> getAllOrders() {
		List<OrderDTO> orders = orderService.findAllOrders();
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, String>> updateOrder(@PathVariable Long id, @RequestBody Order order) {
		Optional<OrderDTO> existingOrder = orderService.findOrderById(id);

		if (existingOrder.isPresent()) {
			order.setId(id);
			Optional<Order> updatedOrder = orderService.updateOrder(id, order);

			if (updatedOrder.isPresent()) {
				Map<String, String> response = new HashMap<>();
				response.put("message", "Order updated successfully!");
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {

			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<Map<String, String>> updateOrderStatus(
			@PathVariable Long id,
			@RequestBody OrderStatus status) {
		Optional<Order> updatedOrder = orderService.updateOrderStatus(id, status);
		
		if (updatedOrder.isPresent()) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Order status updated successfully!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
		boolean canceled = orderService.cancelOrder(id);
		if (canceled) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/checkout/{customerId}")
	public ResponseEntity<Map<String, String>> checkout(@PathVariable Long customerId) {
		Map<String, String> response = new HashMap<>();
		try {
			OrderDTO orderDTO = orderService.proceedToCheckout(customerId);
			response.put("message", "Order created successfully!");
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			response.put("error", e.getMessage());  // Include the error message
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

}
