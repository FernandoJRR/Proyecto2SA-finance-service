package com.sa.finance_service.wallets.infrastructure.persistenceadapter.adapters;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.sa.finance_service.wallets.application.outputports.FindWalletByOwnerIdOutputPort;
import com.sa.finance_service.wallets.domain.Wallet;
import com.sa.finance_service.wallets.infrastructure.persistenceadapter.mappers.WalletRepositoryMapper;
import com.sa.finance_service.wallets.infrastructure.persistenceadapter.repositories.WalletRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FindByOwnerIdAdapter implements FindWalletByOwnerIdOutputPort {

    private final WalletRepository walletRepository;

    @Override
    public Wallet handle(UUID ownerId) {
        return walletRepository.findByOwnerId(ownerId)
            .map(WalletRepositoryMapper.INSTANCE::toDomain)
            .orElseThrow(() -> new IllegalArgumentException("Cartera no encontrada para propietario: " + ownerId));
    }
}
