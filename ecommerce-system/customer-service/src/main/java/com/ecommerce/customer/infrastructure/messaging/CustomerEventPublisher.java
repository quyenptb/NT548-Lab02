package com.ecommerce.customer.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.ecommerce.customer.event.CustomerCreatedEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventPublisher {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Value("${spring.kafka.topics.customer-created}")
    private String customerCreatedTopic;
    
    public void publishCustomerCreatedEvent(CustomerCreatedEvent event) {
        try {
            Message<CustomerCreatedEvent> message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, customerCreatedTopic)
                    .setHeader(KafkaHeaders.KEY, event.getCustomerId().toString())
                    .build();
            
            kafkaTemplate.send(message);
            log.info("Published CustomerCreatedEvent for customer: {}", event.getCustomerId());
        } catch (Exception e) {
            log.error("Failed to publish CustomerCreatedEvent: {}", e.getMessage(), e);
            // Implement retry logic or dead letter queue here
        }
    }
}
