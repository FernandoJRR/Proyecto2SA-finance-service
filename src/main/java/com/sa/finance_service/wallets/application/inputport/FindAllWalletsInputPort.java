package com.sa.finance_service.wallets.application.inputport;

import java.util.List;

import com.sa.finance_service.wallets.domain.Wallet;

public interface FindAllWalletsInputPort {
    public List<Wallet> handle();
}
