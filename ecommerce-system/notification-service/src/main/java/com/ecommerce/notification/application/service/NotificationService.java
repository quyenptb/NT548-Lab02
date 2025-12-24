package com.ecommerce.notification.application.service;

import com.ecommerce.order.domain.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
public class NotificationService {

    public void sendWelcomeEmail(UUID customerId, String email, String fullName) {
        log.info("==================================================");
        log.info("SENDING WELCOME EMAIL");
        log.info("To: {} ({})", fullName, email);
        log.info("Subject: Welcome to Our E-commerce Platform!");
        log.info("Body: Dear {}, thank you for registering...", fullName);
        log.info("==================================================");
    }

    public void sendOrderConfirmation(UUID orderId, UUID customerId, BigDecimal amount) {
        log.info("==================================================");
        log.info("SENDING ORDER CONFIRMATION");
        log.info("Order ID: {}", orderId);
        log.info("Customer ID: {}", customerId);
        log.info("Total Amount: ${}", amount);
        log.info("==================================================");
    }

    public void sendOrderStatusUpdate(UUID orderId, Order.OrderStatus oldStatus, Order.OrderStatus newStatus) {
        log.info("==================================================");
        log.info("SENDING ORDER STATUS UPDATE");
        log.info("Order ID: {}", orderId);
        log.info("Status Change: {} -> {}", oldStatus, newStatus);
        log.info("==================================================");
    }
}