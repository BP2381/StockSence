package com.ims.inventorymanagement.dto;

import com.ims.inventorymanagement.entity.StockMovement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StockMovementSummary {

    private Long id;
    private String productName;
    private String sku;
    private String locationName;
    private StockMovement.MovementType movementType;
    private BigDecimal quantity;
    private String referenceType;
    private Long referenceId;
    private String notes;
    private LocalDateTime createdAt;

    public StockMovementSummary() {
    }

    public StockMovementSummary(
            Long id,
            String productName,
            String sku,
            String locationName,
            StockMovement.MovementType movementType,
            BigDecimal quantity,
            String referenceType,
            Long referenceId,
            String notes,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.productName = productName;
        this.sku = sku;
        this.locationName = locationName;
        this.movementType = movementType;
        this.quantity = quantity;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public StockMovement.MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(StockMovement.MovementType movementType) {
        this.movementType = movementType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}