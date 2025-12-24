package com.ecommerce.order.interfaces.rest.dto;

import com.ecommerce.order.domain.model.Order.OrderStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;



@Data
public class CreateOrderRequest {
    private UUID customerId;
    private String shippingAddress;
    private String billingAddress;
    private List<OrderItemRequest> items;
}