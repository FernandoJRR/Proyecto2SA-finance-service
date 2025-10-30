package com.sa.finance_service.wallets.application.inputport;

import java.util.UUID;

import com.sa.finance_service.wallets.domain.Wallet;
import com.sap.common_lib.exception.NotFoundException;

public interface FindWalletByOwnerIdInputPort {
    public Wallet handle(UUID ownerId) throws NotFoundException;
}
