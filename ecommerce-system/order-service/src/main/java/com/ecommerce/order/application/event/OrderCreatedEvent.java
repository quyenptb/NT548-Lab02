package com.ecommerce.order.application.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ecommerce.order.domain.model.OrderItem;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class OrderCreatedEvent {
    private UUID id;
    private UUID customerId;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String billingAddress;
    private List<OrderItemEvent> items = new ArrayList<>();
    
}







/*
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
 */