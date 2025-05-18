package com.inventorymanagement.controllers;

import com.inventorymanagement.DTO.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inventorymanagement.DTO.LoginRequest;
import com.inventorymanagement.entity.Customer;
import com.inventorymanagement.repository.CustomerRepository;
import com.inventorymanagement.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerRepository customerRepo;

	@PostMapping("/register")
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
		System.out.println (customer.getUsername());
		Customer createdCustomer = customerService.saveCustomer(customer);
		return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<Customer> loginCustomer(@RequestBody LoginRequest loginRequest) {

//		Optional<Customer> customerOptional = customerService.findCustomerById(loginRequest.getId());
		Optional<Customer> customerOptional = customerService.findCustomerByUsername(loginRequest.getUsername());
		System.out.println(loginRequest.getUsername());
		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();

			if (customer.getPassword().equals(loginRequest.getPassword())) {
				
//				customer.setPassword(null);

				return ResponseEntity.ok(customer);
			} else {
				
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		} else {
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
		Optional<Customer> customer = customerService.findCustomerById(id);
		return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = customerService.findAllCustomers();
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
		customerService.deleteCustomer(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

//	@PutMapping("/{id}")
//	public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
//		Optional<Customer> existingCustomerOptional = customerService.findCustomerById(id);
//
//		if (existingCustomerOptional.isPresent()) {
//			Customer existingCustomer = existingCustomerOptional.get();
//
//			existingCustomer.setName(updatedCustomer.getName());
//			existingCustomer.setEmail(updatedCustomer.getEmail());
//			existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
//			existingCustomer.setAddress(updatedCustomer.getAddress());
//			// Do not update password here unless specifically intended
//
//			Customer savedCustomer = customerService.saveCustomer(existingCustomer);
//			return new ResponseEntity<>(savedCustomer, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}

	@PutMapping("/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO updatedCustomer) {
		Optional<Customer> existingCustomerOptional = customerService.findCustomerById(id);

		if (existingCustomerOptional.isPresent()) {
			Customer existingCustomer = existingCustomerOptional.get();

			existingCustomer.setName(updatedCustomer.getName());
			existingCustomer.setEmail(updatedCustomer.getEmail());
			existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
			existingCustomer.setAddress(updatedCustomer.getAddress());
			// Password is NOT modified

			Customer savedCustomer = customerService.saveCustomer(existingCustomer);
			return new ResponseEntity<>(savedCustomer, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/check-username")
	public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
	    boolean exists = customerRepo.existsByUsername(username);
	    Map<String, Boolean> response = new HashMap<>();
	    response.put("exists", exists);
	    return ResponseEntity.ok(response);
	}


}