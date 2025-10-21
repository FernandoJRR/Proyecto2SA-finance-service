package com.sa.finance_service.wallets.application.outputports;

import com.sa.finance_service.wallets.domain.Wallet;

public interface SaveWalletOutputPort {
    public Wallet save(Wallet wallet);
}
