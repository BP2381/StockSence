package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.entity.TransferItem;
import com.ims.inventorymanagement.repository.TransferItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferItemService {

    private final TransferItemRepository transferItemRepository;

    public TransferItemService(TransferItemRepository transferItemRepository) {
        this.transferItemRepository = transferItemRepository;
    }

    public TransferItem createTransferItem(TransferItem transferItem) {
        return transferItemRepository.save(transferItem);
    }

    public Optional<TransferItem> getTransferItemById(Long id) {
        return transferItemRepository.findById(id);
    }

    public List<TransferItem> getAllTransferItems() {
        return transferItemRepository.findAll();
    }

    public List<TransferItem> getItemsByTransferId(Long transferId) {
        return transferItemRepository.findByTransferId(transferId);
    }
}