package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.Transfer;
import com.ims.inventorymanagement.service.TransferService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        return transferService.createTransfer(transfer);
    }

    @GetMapping
    public List<Transfer> getAllTransfers() {
        return transferService.getAllTransfers();
    }

    @GetMapping("/{id}")
    public Optional<Transfer> getTransferById(@PathVariable Long id) {
        return transferService.getTransferById(id);
    }

    @PutMapping("/{id}/validate")
    public Transfer validateTransfer(@PathVariable Long id) {
        return transferService.validateTransfer(id);
    }
}