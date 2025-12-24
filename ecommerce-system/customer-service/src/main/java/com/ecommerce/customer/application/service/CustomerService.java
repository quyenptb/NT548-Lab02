package com.ecommerce.customer.application.service;

import com.ecommerce.customer.domain.model.Customer;
import com.ecommerce.customer.domain.repository.CustomerRepository;
import com.ecommerce.customer.event.CustomerCreatedEvent;
import com.ecommerce.customer.interfaces.rest.dto.CustomerDTO;
import com.ecommerce.customer.interfaces.rest.dto.CreateCustomerRequest;
import com.ecommerce.customer.interfaces.rest.dto.UpdateCustomerRequest;
import com.ecommerce.customer.domain.exception.CustomerNotFoundException;
import com.ecommerce.customer.domain.exception.DuplicateEmailException;
import com.ecommerce.customer.application.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    
    @Transactional
    public CustomerDTO createCustomer(CreateCustomerRequest request) {
        log.info("Creating customer with email: {}", request.getEmail());
        
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + request.getEmail());
        }
        
        Customer customer = Customer.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .isActive(true)
                .build();
        
        Customer savedCustomer = customerRepository.save(customer);
        
        // Publish domain event
        eventPublisher.publishEvent(new CustomerCreatedEvent(
                savedCustomer.getId(),
                savedCustomer.getEmail(),
                savedCustomer.getFirstName(),
                savedCustomer.getLastName(),
                LocalDateTime.now()
        ));
        
        log.info("Customer created successfully with id: {}", savedCustomer.getId());
        return customerMapper.toDTO(savedCustomer);
    }
    
    @Transactional(readOnly = true)
    public CustomerDTO getCustomer(UUID customerId) {
        log.debug("Fetching customer with id: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        return customerMapper.toDTO(customer);
    }
    
    @Transactional
    public CustomerDTO updateCustomer(UUID customerId, UpdateCustomerRequest request) {
        log.info("Updating customer with id: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        
        customerMapper.updateCustomerFromRequest(request, customer);
        Customer updatedCustomer = customerRepository.save(customer);
        
        log.info("Customer updated successfully with id: {}", customerId);
        return customerMapper.toDTO(updatedCustomer);
    }
    
    @Transactional
    public void deactivateCustomer(UUID customerId) {
        log.info("Deactivating customer with id: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        
        customer.setActive(false);
        customerRepository.save(customer);
        
        log.info("Customer deactivated successfully with id: {}", customerId);
    }
    
    @Transactional(readOnly = true)
    public boolean validateCredentials(String email, String password) {
        return customerRepository.findActiveByEmail(email)
                .map(customer -> passwordEncoder.matches(password, customer.getPassword()))
                .orElse(false);
    }
}
