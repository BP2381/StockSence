package com.ims.inventorymanagement.repository;

import com.ims.inventorymanagement.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    Optional<Transfer> findByTransferNumber(String transferNumber);
}