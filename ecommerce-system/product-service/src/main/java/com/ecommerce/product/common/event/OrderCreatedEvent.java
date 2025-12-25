package com.ecommerce.product.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Class này phải nằm trong product-service để ProductEventConsumer có thể sử dụng mà không cần phụ thuộc vào module order-service
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderCreatedEvent {
    private UUID id;
    private UUID customerId;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String billingAddress;
    
    @Builder.Default
    private List<OrderItemEvent> items = new ArrayList<>();
}