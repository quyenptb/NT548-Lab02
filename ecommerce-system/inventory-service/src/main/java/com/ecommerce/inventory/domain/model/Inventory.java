package com.ecommerce.inventory.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "sku"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Inventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "product_id", nullable = false)
    private UUID productId;
    
    @Column(nullable = false)
    private String sku;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "reserved_quantity")
    @Builder.Default
    private Integer reservedQuantity = 0;
    
    @Column(name = "available_quantity")
    private Integer availableQuantity;
    
    @Column(name = "low_stock_threshold")
    private Integer lowStockThreshold = 10;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockStatus stockStatus;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Version
    private Long version;
    
    @PrePersist
    @PreUpdate
    private void calculateAvailableQuantity() {
        this.availableQuantity = Math.max(0, this.quantity - this.reservedQuantity);
        updateStockStatus();
    }
    
    private void updateStockStatus() {
        if (quantity <= 0) {
            stockStatus = StockStatus.OUT_OF_STOCK;
        } else if (availableQuantity <= lowStockThreshold) {
            stockStatus = StockStatus.LOW_STOCK;
        } else {
            stockStatus = StockStatus.IN_STOCK;
        }
    }
    
    public boolean reserveStock(int quantity) {
        if (availableQuantity >= quantity) {
            this.reservedQuantity += quantity;
            calculateAvailableQuantity();
            return true;
        }
        return false;
    }
    
    public boolean releaseStock(int quantity) {
        if (this.reservedQuantity >= quantity) {
            this.reservedQuantity -= quantity;
            calculateAvailableQuantity();
            return true;
        }
        return false;
    }
    
    public boolean updateStock(int newQuantity) {
        if (newQuantity >= 0) {
            this.quantity = newQuantity;
            calculateAvailableQuantity();
            return true;
        }
        return false;
    }
    
    public enum StockStatus {
        IN_STOCK,
        LOW_STOCK,
        OUT_OF_STOCK,
        DISCONTINUED
    }
}
