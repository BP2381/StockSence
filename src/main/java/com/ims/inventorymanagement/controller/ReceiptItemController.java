package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.ReceiptItem;
import com.ims.inventorymanagement.service.ReceiptItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/receipt-items")
public class ReceiptItemController {

    private final ReceiptItemService receiptItemService;

    public ReceiptItemController(ReceiptItemService receiptItemService) {
        this.receiptItemService = receiptItemService;
    }
    @PostMapping
    public ReceiptItem createReceiptItem(@RequestBody ReceiptItem receiptItem) {
        return receiptItemService.createReceiptItem(receiptItem);
    }
    @GetMapping
    public List<ReceiptItem> getAllReceiptItems() {
        return receiptItemService.getAllReceiptItems();
    }
    @GetMapping("/{id}")
    public Optional<ReceiptItem> getReceiptItemById(@PathVariable Long id) {
        return receiptItemService.getReceiptItemById(id);
    }
    @GetMapping("/receipt/{receiptId}")
    public List<ReceiptItem> getItemsByReceiptId(@PathVariable Long receiptId) {
        return receiptItemService.getItemsByReceiptId(receiptId);
    }
}