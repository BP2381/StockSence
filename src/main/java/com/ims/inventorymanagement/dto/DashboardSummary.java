package com.ims.inventorymanagement.dto;

import java.math.BigDecimal;

public class DashboardSummary {

    private long totalProducts;
    private BigDecimal totalStock;
    private long lowStockProducts;
    private long outOfStockProducts;
    private long pendingReceipts;
    private long pendingDeliveries;
    private long pendingTransfers;

    public DashboardSummary() {
    }

    public DashboardSummary(
            long totalProducts,
            BigDecimal totalStock,
            long lowStockProducts,
            long outOfStockProducts,
            long pendingReceipts,
            long pendingDeliveries,
            long pendingTransfers
    ) {
        this.totalProducts = totalProducts;
        this.totalStock = totalStock;
        this.lowStockProducts = lowStockProducts;
        this.outOfStockProducts = outOfStockProducts;
        this.pendingReceipts = pendingReceipts;
        this.pendingDeliveries = pendingDeliveries;
        this.pendingTransfers = pendingTransfers;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public BigDecimal getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(BigDecimal totalStock) {
        this.totalStock = totalStock;
    }

    public long getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(long lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }

    public long getOutOfStockProducts() {
        return outOfStockProducts;
    }

    public void setOutOfStockProducts(long outOfStockProducts) {
        this.outOfStockProducts = outOfStockProducts;
    }

    public long getPendingReceipts() {
        return pendingReceipts;
    }

    public void setPendingReceipts(long pendingReceipts) {
        this.pendingReceipts = pendingReceipts;
    }

    public long getPendingDeliveries() {
        return pendingDeliveries;
    }

    public void setPendingDeliveries(long pendingDeliveries) {
        this.pendingDeliveries = pendingDeliveries;
    }

    public long getPendingTransfers() {
        return pendingTransfers;
    }

    public void setPendingTransfers(long pendingTransfers) {
        this.pendingTransfers = pendingTransfers;
    }
}