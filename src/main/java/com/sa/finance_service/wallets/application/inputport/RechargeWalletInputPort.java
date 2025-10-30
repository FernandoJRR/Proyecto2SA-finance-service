package com.sa.finance_service.wallets.application.inputport;

import java.math.BigDecimal;
import java.util.UUID;

import com.sa.finance_service.wallets.domain.Wallet;

public interface RechargeWalletInputPort {
    public Wallet handle(UUID ownerId, BigDecimal amount);
}
