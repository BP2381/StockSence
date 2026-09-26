package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.entity.Delivery;
import com.ims.inventorymanagement.entity.DeliveryItem;
import com.ims.inventorymanagement.entity.InventoryBalance;
import com.ims.inventorymanagement.entity.StockMovement;
import com.ims.inventorymanagement.repository.DeliveryItemRepository;
import com.ims.inventorymanagement.repository.DeliveryRepository;
import com.ims.inventorymanagement.repository.InventoryBalanceRepository;
import com.ims.inventorymanagement.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryItemRepository deliveryItemRepository;
    private final StockMovementRepository stockMovementRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;

    public DeliveryService(
            DeliveryRepository deliveryRepository,
            DeliveryItemRepository deliveryItemRepository,
            StockMovementRepository stockMovementRepository,
            InventoryBalanceRepository inventoryBalanceRepository
    ) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryItemRepository = deliveryItemRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.inventoryBalanceRepository = inventoryBalanceRepository;
    }

    public Delivery createDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Transactional
    public Delivery validateDelivery(Long deliveryId) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        if (delivery.getStatus() == Delivery.DeliveryStatus.DONE) {
            throw new RuntimeException("Delivery is already validated");
        }

        if (delivery.getStatus() == Delivery.DeliveryStatus.CANCELED) {
            throw new RuntimeException("Canceled delivery cannot be validated");
        }

        List<DeliveryItem> items =
                deliveryItemRepository.findByDeliveryId(deliveryId);

        if (items.isEmpty()) {
            throw new RuntimeException("Delivery has no items");
        }

        for (DeliveryItem item : items) {

            if (item.getQuantity() == null ||
                    item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException(
                        "Delivery item quantity must be greater than zero"
                );
            }

            InventoryBalance balance = inventoryBalanceRepository
                    .findByProductIdAndLocationId(
                            item.getProduct().getId(),
                            item.getLocation().getId()
                    )
                    .orElseThrow(() -> new RuntimeException(
                            "No stock found for product at this location"
                    ));

            if (balance.getQuantity().compareTo(item.getQuantity()) < 0) {
                throw new RuntimeException(
                        "Insufficient stock for product: "
                                + item.getProduct().getSku()
                );
            }

            balance.setQuantity(
                    balance.getQuantity().subtract(item.getQuantity())
            );

            inventoryBalanceRepository.save(balance);

            StockMovement movement = new StockMovement(
                    item.getProduct(),
                    item.getLocation(),
                    StockMovement.MovementType.DELIVERY,
                    item.getQuantity(),
                    "DELIVERY",
                    delivery.getId(),
                    "Delivery " + delivery.getDeliveryNumber()
            );

            stockMovementRepository.save(movement);
        }

        delivery.setStatus(Delivery.DeliveryStatus.DONE);

        return deliveryRepository.save(delivery);
    }

    public Optional<Delivery> getDeliveryById(Long id) {
        return deliveryRepository.findById(id);
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Optional<Delivery> getDeliveryByNumber(String deliveryNumber) {
        return deliveryRepository.findByDeliveryNumber(deliveryNumber);
    }
}
