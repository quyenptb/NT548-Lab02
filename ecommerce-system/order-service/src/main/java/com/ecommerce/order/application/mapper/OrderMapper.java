package com.ecommerce.order.application.mapper;

import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderItem;
import com.ecommerce.order.interfaces.rest.dto.OrderDTO;
import com.ecommerce.order.interfaces.rest.dto.OrderItemDTO;
import com.ecommerce.order.application.event.OrderItemEvent;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        if (order == null) return null;
        
        return OrderDTO.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .billingAddress(order.getBillingAddress())
                .items(toOrderItemDTOs(order.getItems()))
                .createdAt(order.getCreatedAt())
                .build();
    }

    private List<OrderItemDTO> toOrderItemDTOs(List<OrderItem> items) {
        if (items == null) return List.of();
        return items.stream()
                .map(item -> OrderItemDTO.builder()
                        .productId(item.getProductId())
                        .sku(item.getSku())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());
    }

    public List<OrderItemEvent> toOrderItemEvents(List<OrderItem> items) {
        if (items == null) return List.of();
        return items.stream()
                .map(item -> new OrderItemEvent(
                        item.getProductId(),
                        item.getSku(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getProductName())
                )
                .collect(Collectors.toList());
    }
}