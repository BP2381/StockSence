package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.dto.DashboardSummary;
import com.ims.inventorymanagement.entity.Receipt;
import com.ims.inventorymanagement.entity.Delivery;
import com.ims.inventorymanagement.entity.Transfer;
import com.ims.inventorymanagement.repository.DeliveryRepository;
import com.ims.inventorymanagement.repository.InventoryBalanceRepository;
import com.ims.inventorymanagement.repository.ProductRepository;
import com.ims.inventorymanagement.repository.ReceiptRepository;
import com.ims.inventorymanagement.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DashboardService {

    private final ProductRepository productRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final ReceiptRepository receiptRepository;
    private final DeliveryRepository deliveryRepository;
    private final TransferRepository transferRepository;

    public DashboardService(
            ProductRepository productRepository,
            InventoryBalanceRepository inventoryBalanceRepository,
            ReceiptRepository receiptRepository,
            DeliveryRepository deliveryRepository,
            TransferRepository transferRepository
    ) {
        this.productRepository = productRepository;
        this.inventoryBalanceRepository = inventoryBalanceRepository;
        this.receiptRepository = receiptRepository;
        this.deliveryRepository = deliveryRepository;
        this.transferRepository = transferRepository;
    }

    public DashboardSummary getDashboardSummary() {

        long totalProducts = productRepository.count();

        BigDecimal totalStock = inventoryBalanceRepository.findAll()
                .stream()
                .map(balance -> balance.getQuantity())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendingReceipts = receiptRepository.findAll()
                .stream()
                .filter(receipt ->
                        receipt.getStatus() != Receipt.ReceiptStatus.DONE &&
                                receipt.getStatus() != Receipt.ReceiptStatus.CANCELED
                )
                .count();

        long pendingDeliveries = deliveryRepository.findAll()
                .stream()
                .filter(delivery ->
                        delivery.getStatus() != Delivery.DeliveryStatus.DONE &&
                                delivery.getStatus() != Delivery.DeliveryStatus.CANCELED
                )
                .count();

        long pendingTransfers = transferRepository.findAll()
                .stream()
                .filter(transfer ->
                        transfer.getStatus() != Transfer.TransferStatus.DONE &&
                                transfer.getStatus() != Transfer.TransferStatus.CANCELED
                )
                .count();

        long lowStockProducts = 0;
        long outOfStockProducts = 0;

        return new DashboardSummary(
                totalProducts,
                totalStock,
                lowStockProducts,
                outOfStockProducts,
                pendingReceipts,
                pendingDeliveries,
                pendingTransfers
        );
    }
}
