package com.ecommerce.order.application.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ecommerce.order.domain.model.OrderItem;


@Data
@AllArgsConstructor
public class OrderItemEvent {
    private UUID productId;
    private String sku;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String productName;
}

