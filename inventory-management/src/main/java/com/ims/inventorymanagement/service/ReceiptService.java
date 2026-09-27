 package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.entity.InventoryBalance;
import com.ims.inventorymanagement.entity.Receipt;
import com.ims.inventorymanagement.entity.ReceiptItem;
import com.ims.inventorymanagement.entity.StockMovement;
import com.ims.inventorymanagement.repository.InventoryBalanceRepository;
import com.ims.inventorymanagement.repository.ReceiptItemRepository;
import com.ims.inventorymanagement.repository.ReceiptRepository;
import com.ims.inventorymanagement.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptItemRepository receiptItemRepository;
    private final StockMovementRepository stockMovementRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;

    public ReceiptService(
            ReceiptRepository receiptRepository,
            ReceiptItemRepository receiptItemRepository,
            StockMovementRepository stockMovementRepository,
            InventoryBalanceRepository inventoryBalanceRepository
    ) {
        this.receiptRepository = receiptRepository;
        this.receiptItemRepository = receiptItemRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.inventoryBalanceRepository = inventoryBalanceRepository;
    }
    @Transactional
    public Receipt validateReceipt(Long receiptId) {

        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));

        if (receipt.getStatus() == Receipt.ReceiptStatus.DONE) {
            throw new RuntimeException("Receipt is already validated");
        }

        if (receipt.getStatus() == Receipt.ReceiptStatus.CANCELED) {
            throw new RuntimeException("Canceled receipt cannot be validated");
        }

        List<ReceiptItem> items = receiptItemRepository.findByReceiptId(receiptId);

        if (items.isEmpty()) {
            throw new RuntimeException("Receipt has no items");
        }

        for (ReceiptItem item : items) {

            if (item.getQuantity() == null ||
                    item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Receipt item quantity must be greater than zero");
            }

            StockMovement movement = new StockMovement(
                    item.getProduct(),
                    item.getLocation(),
                    StockMovement.MovementType.RECEIPT,
                    item.getQuantity(),
                    "RECEIPT",
                    receipt.getId(),
                    "Receipt " + receipt.getReceiptNumber()
            );

            stockMovementRepository.save(movement);

            InventoryBalance balance = inventoryBalanceRepository
                    .findByProductIdAndLocationId(
                            item.getProduct().getId(),
                            item.getLocation().getId()
                    )
                    .orElseGet(() -> new InventoryBalance(
                            item.getProduct(),
                            item.getLocation(),
                            BigDecimal.ZERO
                    ));

            balance.setQuantity(
                    balance.getQuantity().add(item.getQuantity())
            );

            inventoryBalanceRepository.save(balance);
        }

        receipt.setStatus(Receipt.ReceiptStatus.DONE);

        return receiptRepository.save(receipt);
    }

    public Receipt createReceipt(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    public Optional<Receipt> getReceiptById(Long id) {
        return receiptRepository.findById(id);
    }

    public List<Receipt> getAllReceipts() {
        return receiptRepository.findAll();
    }

    public Optional<Receipt> getReceiptByNumber(String receiptNumber) {
        return receiptRepository.findByReceiptNumber(receiptNumber);
    }
}