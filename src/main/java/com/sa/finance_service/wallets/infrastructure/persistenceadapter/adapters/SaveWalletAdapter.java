package com.sa.finance_service.wallets.infrastructure.persistenceadapter.adapters;

import org.springframework.stereotype.Component;

import com.sa.finance_service.wallets.application.outputports.SaveWalletOutputPort;
import com.sa.finance_service.wallets.domain.Wallet;
import com.sa.finance_service.wallets.infrastructure.persistenceadapter.mappers.WalletRepositoryMapper;
import com.sa.finance_service.wallets.infrastructure.persistenceadapter.models.WalletEntity;
import com.sa.finance_service.wallets.infrastructure.persistenceadapter.repositories.WalletRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SaveWalletAdapter implements SaveWalletOutputPort {

    private final WalletRepository walletRepository;

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity walletEntity = WalletRepositoryMapper.INSTANCE.toEntity(wallet);
        WalletEntity savedWallet = walletRepository.save(walletEntity);
        return WalletRepositoryMapper.INSTANCE.toDomain(savedWallet);
    }
}
