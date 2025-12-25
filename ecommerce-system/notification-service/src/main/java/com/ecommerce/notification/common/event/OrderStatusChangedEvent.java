package com.ecommerce.notification.common.event;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ecommerce.notification.common.model.Order;

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