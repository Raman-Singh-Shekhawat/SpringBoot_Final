package com.inventorymanagement.service;

import com.inventorymanagement.entity.Customer;
import com.inventorymanagement.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveCustomer_Success() {
        Customer customer = new Customer();
        customer.setName("Test User");
        customer.setEmail("test@test.com");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.saveCustomer(customer);

        assertNotNull(savedCustomer);
        assertEquals("Test User", savedCustomer.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void findCustomerById_Success() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test User");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> foundCustomer = customerService.findCustomerById(1L);

        assertTrue(foundCustomer.isPresent());
        assertEquals("Test User", foundCustomer.get().getName());
    }

    @Test
    void findAllCustomers_Success() {
        Customer customer1 = new Customer();
        customer1.setName("User 1");
        Customer customer2 = new Customer();
        customer2.setName("User 2");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        List<Customer> customers = customerService.findAllCustomers();

        assertEquals(2, customers.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void deleteCustomer_Success() {
        Long customerId = 1L;
        doNothing().when(customerRepository).deleteById(customerId);

        customerService.deleteCustomer(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }
}