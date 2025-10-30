package com.sa.finance_service.wallets.infrastructure.persistenceadapter.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.finance_service.wallets.infrastructure.persistenceadapter.models.WalletEntity;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {
    Optional<WalletEntity> findByOwnerId(UUID ownerId);
}
