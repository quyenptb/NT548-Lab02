package com.ecommerce.payment.application.service;

import com.ecommerce.payment.domain.model.Payment;
import com.ecommerce.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public void createPendingPayment(UUID orderId, UUID customerId, BigDecimal amount, LocalDateTime orderDate) {
        log.info("Creating pending payment for Order: {}", orderId);
        
        Payment payment = Payment.builder()
                .orderId(orderId)
                .customerId(customerId)
                .amount(amount)
                .status(Payment.PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);
        log.info("Payment record created successfully.");
        
        // Mock processing payment immediately for demo purposes
        processPayment(payment);
    }

    private void processPayment(Payment payment) {
        // Logic giả lập thanh toán
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        paymentRepository.save(payment);
        log.info("Payment processed successfully for Order: {}", payment.getOrderId());
    }
}