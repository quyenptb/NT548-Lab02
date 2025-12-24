package com.ecommerce.customer.application.mapper;

import com.ecommerce.customer.domain.model.Customer;
import com.ecommerce.customer.interfaces.rest.dto.CustomerDTO;
import com.ecommerce.customer.interfaces.rest.dto.UpdateCustomerRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;
        
        return CustomerDTO.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .isActive(customer.isActive())
                .loyaltyPoints(customer.getLoyaltyPoints())
                .build();
    }

    public void updateCustomerFromRequest(UpdateCustomerRequest request, Customer customer) {
        if (request == null || customer == null) return;

        if (request.getFirstName() != null) customer.setFirstName(request.getFirstName());
        if (request.getLastName() != null) customer.setLastName(request.getLastName());
        if (request.getPhone() != null) customer.setPhone(request.getPhone());
        if (request.getAddress() != null) customer.setAddress(request.getAddress());
    }
}