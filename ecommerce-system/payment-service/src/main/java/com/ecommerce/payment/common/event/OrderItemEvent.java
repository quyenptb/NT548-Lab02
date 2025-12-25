package com.ecommerce.payment.common.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

// Class bổ trợ cần thiết cho OrderCreatedEvent bên trên
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEvent {
    private UUID productId;
    private String sku;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String productName;
}