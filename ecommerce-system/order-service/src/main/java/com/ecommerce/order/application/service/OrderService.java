package com.ecommerce.order.application.service;


import com.ecommerce.order.application.event.OrderCreatedEvent;
import com.ecommerce.order.application.event.OrderStatusChangedEvent;
import com.ecommerce.order.domain.exception.OrderNotFoundException;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderItem;
import com.ecommerce.order.domain.repository.OrderRepository;
import com.ecommerce.order.interfaces.rest.dto.OrderDTO;
import com.ecommerce.order.application.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ApplicationEventPublisher eventPublisher;
    
    @Transactional
    public OrderDTO createOrder(OrderCreatedEvent request) {
        log.info("Creating order for customer: {}", request.getCustomerId());
        
        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .status(Order.OrderStatus.PENDING)
                .shippingAddress(request.getShippingAddress())
                .billingAddress(request.getBillingAddress())
                .build();
        
        request.getItems().forEach(itemRequest -> {
            OrderItem item = OrderItem.builder()
                    .productId(itemRequest.getProductId())
                    .sku(itemRequest.getSku())
                    .productName(itemRequest.getProductName())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(itemRequest.getUnitPrice())
                    .subtotal(itemRequest.getUnitPrice()
                            .multiply(BigDecimal.valueOf(itemRequest.getQuantity())))
                    .build();
            order.addItem(item);
        });
        
        order.calculateTotal();
        order.setPaymentDueDate(LocalDateTime.now().plusDays(2));
        
        Order savedOrder = orderRepository.save(order);
        
        // Publish domain event
        OrderCreatedEvent event = 
        OrderCreatedEvent.builder().id(savedOrder.getId()).customerId(savedOrder.getCustomerId()).totalAmount(savedOrder.getTotalAmount()).items(orderMapper.toOrderItemEvents(savedOrder.getItems())).build();
        
        
        eventPublisher.publishEvent(event);
        
        log.info("Order created successfully with id: {}", savedOrder.getId());
        return orderMapper.toDTO(savedOrder);
    }
    
    @Transactional(readOnly = true)
    public OrderDTO getOrder(UUID orderId) {
        log.debug("Fetching order with id: {}", orderId);
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return orderMapper.toDTO(order);
    }
    
    @Transactional
    public OrderDTO updateOrderStatus(UUID orderId, Order.OrderStatus newStatus) {
        log.info("Updating order {} status to {}", orderId, newStatus);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        Order.OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        
        // Publish status change event
        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                orderId,
                oldStatus,
                newStatus,
                LocalDateTime.now()
        ));
        
        log.info("Order {} status updated from {} to {}", 
                orderId, oldStatus, newStatus);
        return orderMapper.toDTO(updatedOrder);
    }
    
    @Transactional(readOnly = true)
    public List<OrderDTO> getCustomerOrders(UUID customerId) {
        log.debug("Fetching orders for customer: {}", customerId);
        return orderRepository.findByCustomerId(customerId).stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }
}
