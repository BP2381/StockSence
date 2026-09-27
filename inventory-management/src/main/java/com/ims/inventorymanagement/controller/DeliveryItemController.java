package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.DeliveryItem;
import com.ims.inventorymanagement.service.DeliveryItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/delivery-items")
public class DeliveryItemController {

    private final DeliveryItemService deliveryItemService;

    public DeliveryItemController(DeliveryItemService deliveryItemService) {
        this.deliveryItemService = deliveryItemService;
    }

    @PostMapping
    public DeliveryItem createDeliveryItem(
            @RequestBody DeliveryItem deliveryItem
    ) {
        return deliveryItemService.createDeliveryItem(deliveryItem);
    }

    @GetMapping
    public List<DeliveryItem> getAllDeliveryItems() {
        return deliveryItemService.getAllDeliveryItems();
    }

    @GetMapping("/{id}")
    public Optional<DeliveryItem> getDeliveryItemById(
            @PathVariable Long id
    ) {
        return deliveryItemService.getDeliveryItemById(id);
    }

    @GetMapping("/delivery/{deliveryId}")
    public List<DeliveryItem> getItemsByDeliveryId(
            @PathVariable Long deliveryId
    ) {
        return deliveryItemService.getItemsByDeliveryId(deliveryId);
    }
}