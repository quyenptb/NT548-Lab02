package com.ecommerce.customer.interfaces.rest.dto;

import com.ecommerce.customer.domain.model.Customer.Address;
import lombok.Data;

@Data
public class UpdateCustomerRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private Address address;
}