package com.ecommerce.inventory.application.service;

import com.ecommerce.inventory.domain.model.Inventory;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String sku) {
        return inventoryRepository.findBySku(sku)
                .map(inventory -> inventory.getStockStatus() == Inventory.StockStatus.IN_STOCK)
                .orElse(false);
    }

    @Transactional
    public void updateStock(UUID productId, int quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));
        
        inventory.updateStock(quantity);
        inventoryRepository.save(inventory);
        log.info("Updated stock for product {} to {}", productId, quantity);
    }
}