package com.sa.finance_service.wallets.application.outputports;

import java.util.UUID;

import com.sa.finance_service.wallets.domain.Wallet;

public interface FindWalletByOwnerIdOutputPort {
    public Wallet handle(UUID ownerId);
}
