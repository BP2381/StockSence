package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.dto.DashboardSummary;
import com.ims.inventorymanagement.entity.Delivery;
import com.ims.inventorymanagement.entity.Receipt;
import com.ims.inventorymanagement.entity.ReorderRule;
import com.ims.inventorymanagement.entity.Transfer;
import com.ims.inventorymanagement.repository.DeliveryRepository;
import com.ims.inventorymanagement.repository.InventoryBalanceRepository;
import com.ims.inventorymanagement.repository.ProductRepository;
import com.ims.inventorymanagement.repository.ReceiptRepository;
import com.ims.inventorymanagement.repository.ReorderRuleRepository;
import com.ims.inventorymanagement.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private final ProductRepository productRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final ReceiptRepository receiptRepository;
    private final DeliveryRepository deliveryRepository;
    private final TransferRepository transferRepository;
    private final ReorderRuleRepository reorderRuleRepository;

    public DashboardService(
            ProductRepository productRepository,
            InventoryBalanceRepository inventoryBalanceRepository,
            ReceiptRepository receiptRepository,
            DeliveryRepository deliveryRepository,
            TransferRepository transferRepository,
            ReorderRuleRepository reorderRuleRepository
    ) {
        this.productRepository = productRepository;
        this.inventoryBalanceRepository = inventoryBalanceRepository;
        this.receiptRepository = receiptRepository;
        this.deliveryRepository = deliveryRepository;
        this.transferRepository = transferRepository;
        this.reorderRuleRepository = reorderRuleRepository;
    }

    public DashboardSummary getDashboardSummary() {

        long totalProducts = productRepository.count();

        BigDecimal totalStock = inventoryBalanceRepository.findAll()
                .stream()
                .map(balance -> balance.getQuantity())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<ReorderRule> activeRules = reorderRuleRepository.findAll()
                .stream()
                .filter(rule -> Boolean.TRUE.equals(rule.getActive()))
                .toList();

        long lowStockProducts = activeRules.stream()
                .filter(rule -> {
                    BigDecimal currentStock = inventoryBalanceRepository
                            .findByProductIdAndLocationId(
                                    rule.getProduct().getId(),
                                    rule.getLocation().getId()
                            )
                            .map(balance -> balance.getQuantity())
                            .orElse(BigDecimal.ZERO);

                    return currentStock.compareTo(rule.getReorderLevel()) <= 0
                            && currentStock.compareTo(BigDecimal.ZERO) > 0;
                })
                .map(rule -> rule.getProduct().getId())
                .distinct()
                .count();

        long outOfStockProducts = activeRules.stream()
                .filter(rule -> {
                    BigDecimal currentStock = inventoryBalanceRepository
                            .findByProductIdAndLocationId(
                                    rule.getProduct().getId(),
                                    rule.getLocation().getId()
                            )
                            .map(balance -> balance.getQuantity())
                            .orElse(BigDecimal.ZERO);

                    return currentStock.compareTo(BigDecimal.ZERO) == 0;
                })
                .map(rule -> rule.getProduct().getId())
                .distinct()
                .count();

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