package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.Receipt;
import com.ims.inventorymanagement.service.ReceiptService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping
    public Receipt createReceipt(@RequestBody Receipt receipt) {
        return receiptService.createReceipt(receipt);
    }

    @GetMapping
    public List<Receipt> getAllReceipts() {
        return receiptService.getAllReceipts();
    }

    @GetMapping("/{id}")
    public Optional<Receipt> getReceiptById(@PathVariable Long id) {
        return receiptService.getReceiptById(id);
    }
}