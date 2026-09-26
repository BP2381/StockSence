package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.entity.DeliveryItem;
import com.ims.inventorymanagement.repository.DeliveryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryItemService {

    private final DeliveryItemRepository deliveryItemRepository;

    public DeliveryItemService(DeliveryItemRepository deliveryItemRepository) {
        this.deliveryItemRepository = deliveryItemRepository;
    }

    public DeliveryItem createDeliveryItem(DeliveryItem deliveryItem) {
        return deliveryItemRepository.save(deliveryItem);
    }

    public Optional<DeliveryItem> getDeliveryItemById(Long id) {
        return deliveryItemRepository.findById(id);
    }

    public List<DeliveryItem> getAllDeliveryItems() {
        return deliveryItemRepository.findAll();
    }

    public List<DeliveryItem> getItemsByDeliveryId(Long deliveryId) {
        return deliveryItemRepository.findByDeliveryId(deliveryId);
    }
}