package com.sa.finance_service.wallets.infrastructure.persistenceadapter.adapters;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sa.finance_service.wallets.application.outputports.FindAllWalletsOutputPort;
import com.sa.finance_service.wallets.domain.Wallet;
import com.sa.finance_service.wallets.infrastructure.persistenceadapter.mappers.WalletRepositoryMapper;
import com.sa.finance_service.wallets.infrastructure.persistenceadapter.repositories.WalletRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FindAllWalletsAdapter implements FindAllWalletsOutputPort {

    private final WalletRepository walletRepository;

    @Override
    public List<Wallet> handle() {
        return walletRepository
            .findAll()
            .stream()
            .map(WalletRepositoryMapper.INSTANCE::toDomain)
            .toList();
    }
}
