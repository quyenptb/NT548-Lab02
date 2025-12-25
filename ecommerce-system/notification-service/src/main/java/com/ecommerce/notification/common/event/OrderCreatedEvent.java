package com.ecommerce.notification.common.event;



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
    private LocalDateTime createdTime = LocalDateTime.now();;
    
}


