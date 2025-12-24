package com.ecommerce.order.application.event;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ecommerce.order.domain.model.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusChangedEvent {
    private UUID orderId;
    private Order.OrderStatus oldStatus;
    private Order.OrderStatus newStatus;
    private LocalDateTime createdAt;
    
}

