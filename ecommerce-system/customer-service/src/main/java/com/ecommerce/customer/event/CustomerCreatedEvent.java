package com.ecommerce.customer.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreatedEvent {
    private UUID customerId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime timestamp = LocalDateTime.now();
}
