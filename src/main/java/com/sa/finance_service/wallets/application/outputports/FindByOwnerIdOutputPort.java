package com.sa.finance_service.wallets.application.outputports;

import java.util.UUID;

import com.sa.finance_service.wallets.domain.OwnerType;
import com.sa.finance_service.wallets.domain.Wallet;

public interface FindByOwnerIdOutputPort {
    public Wallet handle(UUID ownerId);
}
