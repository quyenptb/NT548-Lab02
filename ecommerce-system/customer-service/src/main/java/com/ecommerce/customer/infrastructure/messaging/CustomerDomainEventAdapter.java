package com.ecommerce.customer.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ecommerce.customer.event.CustomerCreatedEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerDomainEventAdapter {

    private final CustomerEventPublisher kafkaPublisher;

    // @TransactionalEventListener đảm bảo chỉ bắn Kafka KHI transaction DB đã commit thành công.
    // Nếu lưu DB thất bại, event này sẽ không được bắn -> Đảm bảo tính nhất quán.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCustomerCreatedEvent(CustomerCreatedEvent event) {
        log.info("Domain event received: CustomerCreatedEvent for ID: {}. Forwarding to Kafka...", event.getCustomerId());
        kafkaPublisher.publishCustomerCreatedEvent(event);
    }
}