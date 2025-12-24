package com.ecommerce.customer.interfaces.rest;

import com.ecommerce.customer.application.service.CustomerService;
import com.ecommerce.customer.interfaces.rest.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {
    
    private final CustomerService customerService;
    
    @PostMapping
    @Operation(summary = "Create a new customer")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        CustomerDTO customerDTO = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO);
    }
    
    @GetMapping("/{customerId}")
    @Operation(summary = "Get customer by ID")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable UUID customerId) {
        CustomerDTO customerDTO = customerService.getCustomer(customerId);
        return ResponseEntity.ok(customerDTO);
    }
    
    @PutMapping("/{customerId}")
    @Operation(summary = "Update customer")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable UUID customerId,
            @Valid @RequestBody UpdateCustomerRequest request) {
        CustomerDTO customerDTO = customerService.updateCustomer(customerId, request);
        return ResponseEntity.ok(customerDTO);
    }
    
    @DeleteMapping("/{customerId}")
    @Operation(summary = "Deactivate customer")
    public ResponseEntity<Void> deactivateCustomer(@PathVariable UUID customerId) {
        customerService.deactivateCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
