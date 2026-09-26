package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.entity.Location;
import com.ims.inventorymanagement.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public List<Location> getLocationsByWarehouseId(Long warehouseId) {
        return locationRepository.findByWarehouseId(warehouseId);
    }
}