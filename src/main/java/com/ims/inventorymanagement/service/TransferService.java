package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.entity.InventoryBalance;
import com.ims.inventorymanagement.entity.StockMovement;
import com.ims.inventorymanagement.entity.Transfer;
import com.ims.inventorymanagement.entity.TransferItem;
import com.ims.inventorymanagement.repository.InventoryBalanceRepository;
import com.ims.inventorymanagement.repository.StockMovementRepository;
import com.ims.inventorymanagement.repository.TransferItemRepository;
import com.ims.inventorymanagement.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final TransferItemRepository transferItemRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final StockMovementRepository stockMovementRepository;

    public TransferService(
            TransferRepository transferRepository,
            TransferItemRepository transferItemRepository,
            InventoryBalanceRepository inventoryBalanceRepository,
            StockMovementRepository stockMovementRepository
    ) {
        this.transferRepository = transferRepository;
        this.transferItemRepository = transferItemRepository;
        this.inventoryBalanceRepository = inventoryBalanceRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    public Transfer createTransfer(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    @Transactional
    public Transfer validateTransfer(Long transferId) {

        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new RuntimeException("Transfer not found"));

        if (transfer.getStatus() == Transfer.TransferStatus.DONE) {
            throw new RuntimeException("Transfer is already validated");
        }

        if (transfer.getStatus() == Transfer.TransferStatus.CANCELED) {
            throw new RuntimeException("Canceled transfer cannot be validated");
        }

        List<TransferItem> items =
                transferItemRepository.findByTransferId(transferId);

        if (items.isEmpty()) {
            throw new RuntimeException("Transfer has no items");
        }

        for (TransferItem item : items) {

            if (item.getQuantity() == null ||
                    item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException(
                        "Transfer item quantity must be greater than zero"
                );
            }

            if (item.getFromLocation().getId()
                    .equals(item.getToLocation().getId())) {
                throw new RuntimeException(
                        "Source and destination locations must be different"
                );
            }

            InventoryBalance sourceBalance =
                    inventoryBalanceRepository
                            .findByProductIdAndLocationId(
                                    item.getProduct().getId(),
                                    item.getFromLocation().getId()
                            )
                            .orElseThrow(() -> new RuntimeException(
                                    "No stock found for product at source location"
                            ));

            if (sourceBalance.getQuantity()
                    .compareTo(item.getQuantity()) < 0) {
                throw new RuntimeException(
                        "Insufficient stock for product: "
                                + item.getProduct().getSku()
                );
            }

            sourceBalance.setQuantity(
                    sourceBalance.getQuantity()
                            .subtract(item.getQuantity())
            );

            inventoryBalanceRepository.save(sourceBalance);

            InventoryBalance destinationBalance =
                    inventoryBalanceRepository
                            .findByProductIdAndLocationId(
                                    item.getProduct().getId(),
                                    item.getToLocation().getId()
                            )
                            .orElseGet(() -> new InventoryBalance(
                                    item.getProduct(),
                                    item.getToLocation(),
                                    BigDecimal.ZERO
                            ));

            destinationBalance.setQuantity(
                    destinationBalance.getQuantity()
                            .add(item.getQuantity())
            );

            inventoryBalanceRepository.save(destinationBalance);

            StockMovement outgoingMovement = new StockMovement(
                    item.getProduct(),
                    item.getFromLocation(),
                    StockMovement.MovementType.TRANSFER_OUT,
                    item.getQuantity(),
                    "TRANSFER",
                    transfer.getId(),
                    "Transfer " + transfer.getTransferNumber()
            );

            stockMovementRepository.save(outgoingMovement);

            StockMovement incomingMovement = new StockMovement(
                    item.getProduct(),
                    item.getToLocation(),
                    StockMovement.MovementType.TRANSFER_IN,
                    item.getQuantity(),
                    "TRANSFER",
                    transfer.getId(),
                    "Transfer " + transfer.getTransferNumber()
            );

            stockMovementRepository.save(incomingMovement);
        }

        transfer.setStatus(Transfer.TransferStatus.DONE);

        return transferRepository.save(transfer);
    }

    public Optional<Transfer> getTransferById(Long id) {
        return transferRepository.findById(id);
    }

    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    public Optional<Transfer> getTransferByNumber(String transferNumber) {
        return transferRepository.findByTransferNumber(transferNumber);
    }
}