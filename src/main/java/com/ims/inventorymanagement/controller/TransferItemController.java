package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.TransferItem;
import com.ims.inventorymanagement.service.TransferItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transfer-items")
public class TransferItemController {

    private final TransferItemService transferItemService;

    public TransferItemController(TransferItemService transferItemService) {
        this.transferItemService = transferItemService;
    }

    @PostMapping
    public TransferItem createTransferItem(
            @RequestBody TransferItem transferItem
    ) {
        return transferItemService.createTransferItem(transferItem);
    }

    @GetMapping
    public List<TransferItem> getAllTransferItems() {
        return transferItemService.getAllTransferItems();
    }

    @GetMapping("/{id}")
    public Optional<TransferItem> getTransferItemById(
            @PathVariable Long id
    ) {
        return transferItemService.getTransferItemById(id);
    }

    @GetMapping("/transfer/{transferId}")
    public List<TransferItem> getItemsByTransferId(
            @PathVariable Long transferId
    ) {
        return transferItemService.getItemsByTransferId(transferId);
    }
}
