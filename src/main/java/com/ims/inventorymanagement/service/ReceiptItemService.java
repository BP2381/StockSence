package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.entity.ReceiptItem;
import com.ims.inventorymanagement.repository.ReceiptItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiptItemService {

    private final ReceiptItemRepository receiptItemRepository;

    public ReceiptItemService(ReceiptItemRepository receiptItemRepository) {
        this.receiptItemRepository = receiptItemRepository;
    }

    public ReceiptItem createReceiptItem(ReceiptItem receiptItem) {
        return receiptItemRepository.save(receiptItem);
    }

    public Optional<ReceiptItem> getReceiptItemById(Long id) {
        return receiptItemRepository.findById(id);
    }

    public List<ReceiptItem> getAllReceiptItems() {
        return receiptItemRepository.findAll();
    }

    public List<ReceiptItem> getItemsByReceiptId(Long receiptId) {
        return receiptItemRepository.findByReceiptId(receiptId);
    }
}