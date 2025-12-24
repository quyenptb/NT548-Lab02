package com.ecommerce.customer.interfaces.rest.dto;

import com.ecommerce.customer.domain.model.Customer.Address;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class CustomerDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Address address;
    private boolean isActive;
    private int loyaltyPoints;
}