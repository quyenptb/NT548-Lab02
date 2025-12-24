package com.ecommerce.payment.infrastructure.messaging;

import com.ecommerce.payment.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final PaymentService paymentService;

    @KafkaListener(
            topics = "${spring.kafka.topics.order-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleOrderCreatedEvent(@Payload OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for order: {}", event.getOrderId());

        try {
            paymentService.createPendingPayment(
                    event.getOrderId(),
                    event.getCustomerId(),
                    event.getTotalAmount(),
                    event.getTimestamp()
            );
            log.info("Created pending payment for order: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("Failed to create payment for order {}: {}", event.getOrderId(), e.getMessage());
            // Tương tự, nếu lỗi thanh toán khởi tạo, cần ném exception
            // để Kafka xử lý lại hoặc hệ thống Saga kích hoạt luồng bồi hoàn (Compensating Transaction)
            throw new RuntimeException("Payment creation failed", e);
        }
    }
}