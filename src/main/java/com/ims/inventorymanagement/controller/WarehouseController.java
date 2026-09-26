package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.Warehouse;
import com.ims.inventorymanagement.service.WarehouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    public Warehouse createWarehouse(@RequestBody Warehouse warehouse) {
        return warehouseService.createWarehouse(warehouse);
    }

    @GetMapping
    public List<Warehouse> getAllWarehouses() {
        return warehouseService.getAllWarehouses();
    }

    @GetMapping("/{id}")
    public Optional<Warehouse> getWarehouseById(@PathVariable Long id) {
        return warehouseService.getWarehouseById(id);
    }

    @GetMapping("/code/{code}")
    public Optional<Warehouse> getWarehouseByCode(@PathVariable String code) {
        return warehouseService.getWarehouseByCode(code);
    }
}