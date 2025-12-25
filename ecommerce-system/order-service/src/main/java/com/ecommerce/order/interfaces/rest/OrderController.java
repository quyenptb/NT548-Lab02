package com.ecommerce.order.interfaces.rest;

import com.ecommerce.order.application.event.OrderCreatedEvent;
import com.ecommerce.order.application.event.OrderItemEvent;
import com.ecommerce.order.application.service.OrderService;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.interfaces.rest.dto.CreateOrderRequest;
import com.ecommerce.order.interfaces.rest.dto.OrderDTO;
import com.ecommerce.order.interfaces.rest.dto.OrderItemRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.info("REST request to create order for customer: {}", request.getCustomerId());

        // Mapping từ Request DTO sang Input Object mà Service đang mong đợi
        OrderCreatedEvent serviceInput = mapToServiceInput(request);

        OrderDTO createdOrder = orderService.createOrder(serviceInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getCustomerOrders(@PathVariable UUID customerId) {
        return ResponseEntity.ok(orderService.getCustomerOrders(customerId));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestParam Order.OrderStatus status) {
        log.info("REST request to update order {} status to {}", orderId, status);
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    // --- Helper Mapping Methods ---

    private OrderCreatedEvent mapToServiceInput(CreateOrderRequest request) {
        List<OrderItemEvent> itemEvents = request.getItems() != null 
            ? request.getItems().stream()
                .map(this::mapToItemEvent)
                .collect(Collectors.toList())
            : Collections.emptyList();

        return OrderCreatedEvent.builder()
                .customerId(request.getCustomerId())
                .shippingAddress(request.getShippingAddress())
                .billingAddress(request.getBillingAddress())
                .items(itemEvents)
                .build();
    }

    private OrderItemEvent mapToItemEvent(OrderItemRequest itemRequest) {
        return new OrderItemEvent(
                itemRequest.getProductId(),
                itemRequest.getSku(),
                itemRequest.getQuantity(),
                itemRequest.getUnitPrice(),
                itemRequest.getProductName()
        );
    }
}