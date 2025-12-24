package com.ecommerce.notification.infrastructure.messaging;

import com.ecommerce.customer.application.event.CustomerCreatedEvent;
import com.ecommerce.order.application.event.OrderCreatedEvent;
import com.ecommerce.order.application.event.OrderStatusChangedEvent;
import com.ecommerce.notification.application.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {
    
    private final NotificationService notificationService;
    
    @KafkaListener(
            topics = "${spring.kafka.topics.customer-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleCustomerCreatedEvent(@Payload CustomerCreatedEvent event) {
        log.info("Received CustomerCreatedEvent for customer: {}", event.getCustomerId());
        
        // Send welcome email
        notificationService.sendWelcomeEmail(
                event.getCustomerId(),
                event.getEmail(),
                event.getFirstName() + " " + event.getLastName()
        );
    }
    
    @KafkaListener(
            topics = "${spring.kafka.topics.order-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleOrderCreatedEvent(@Payload OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for order: {}", event.getOrderId());
        
        // Send order confirmation
        notificationService.sendOrderConfirmation(
                event.getOrderId(),
                event.getCustomerId(),
                event.getTotalAmount()
        );
    }
    
    @KafkaListener(
            topics = "${spring.kafka.topics.order-status-changed}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleOrderStatusChangedEvent(@Payload OrderStatusChangedEvent event) {
        log.info("Received OrderStatusChangedEvent for order: {}", event.getOrderId());
        
        // Send status update notification
        notificationService.sendOrderStatusUpdate(
                event.getOrderId(),
                event.getOldStatus(),
                event.getNewStatus()
        );
    }
}
