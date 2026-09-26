package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.Location;
import com.ims.inventorymanagement.service.LocationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public Location createLocation(@RequestBody Location location) {
        return locationService.createLocation(location);
    }

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    public Optional<Location> getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<Location> getLocationsByWarehouseId(@PathVariable Long warehouseId) {
        return locationService.getLocationsByWarehouseId(warehouseId);
    }
}