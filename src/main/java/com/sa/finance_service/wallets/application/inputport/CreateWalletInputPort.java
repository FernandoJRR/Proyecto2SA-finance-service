package com.sa.finance_service.wallets.application.inputport;

import com.sa.finance_service.wallets.application.dtos.CreateWalletDTO;
import com.sa.finance_service.wallets.domain.Wallet;

public interface CreateWalletInputPort {
    public Wallet handle(CreateWalletDTO createWalletDTO);
}
