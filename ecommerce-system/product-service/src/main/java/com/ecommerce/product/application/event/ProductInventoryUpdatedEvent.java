package com.ecommerce.product.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventoryUpdatedEvent {
    private UUID productId;
    private String sku;
    private Integer oldQuantity;
    private Integer newQuantity;
    private BigDecimal price;
    private String eventType = "INVENTORY_UPDATED";
}
