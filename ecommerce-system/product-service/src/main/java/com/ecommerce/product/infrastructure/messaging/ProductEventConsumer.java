package com.ecommerce.product.infrastructure.messaging;

import com.ecommerce.product.application.service.ProductService;
import com.ecommerce.product.common.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventConsumer {

    private final ProductService productService;

    @KafkaListener(
            topics = "${spring.kafka.topics.order-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    @Transactional // Quan trọng: Đảm bảo toàn bộ việc trừ kho nằm trong 1 transaction DB
    public void handleOrderCreatedEvent(@Payload OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for order: {}", event.getOrderId());

        try {
            // Xử lý tuần tự, nếu 1 cái lỗi sẽ nhảy xuống catch
            for (var item : event.getItems()) {
                productService.reserveStock(item.getProductId(), item.getQuantity());
                log.info("Reserved {} units of product: {}", item.getQuantity(), item.getProductId());
            }
        } catch (Exception e) {
            log.error("Failed to reserve stock for order {}. Rolling back transaction.", event.getOrderId(), e);
            
            // QUAN TRỌNG: Phải ném lỗi ra ngoài để Transaction Manager rollback lại các item đã trừ trước đó.
            // Nếu nuốt lỗi (try-catch mà không throw), transaction sẽ commit -> sai lệch kho.
            // Việc ném lỗi cũng giúp Kafka biết message này xử lý thất bại để retry hoặc đưa vào Dead Letter Queue.
            throw new RuntimeException("Stock reservation failed", e);
        }
    }
}
