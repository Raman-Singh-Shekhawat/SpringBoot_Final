package com.inventorymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.inventorymanagement.DTO.CustomerDTO;
import com.inventorymanagement.entity.Customer;
import com.inventorymanagement.exception.UsernameAlreadyExistsException;
import com.inventorymanagement.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	CustomerDTO customerDTO = new CustomerDTO();
	String username = customerDTO.getUsername();

	public Customer saveCustomer(Customer customer) throws UsernameAlreadyExistsException {
		
        if (customerRepository.existsByUsername(customerDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
		return customerRepository.save(customer);
	}

	public Optional<Customer> findCustomerById(Long id) {
		return customerRepository.findById(id);
	}

	public List<Customer> findAllCustomers() {
		return customerRepository.findAll();
	}

	public void deleteCustomer(Long id) {
		customerRepository.deleteById(id);
	}

	public Optional<Customer> findCustomerByUsername(String username) {
		// TODO Auto-generated method stub
		return customerRepository.findByUsername (username);
	}

}