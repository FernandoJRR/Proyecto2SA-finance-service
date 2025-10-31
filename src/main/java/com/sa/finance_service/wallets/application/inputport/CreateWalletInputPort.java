package com.sa.finance_service.wallets.application.inputport;

import com.sa.finance_service.wallets.application.dtos.CreateWalletDTO;
import com.sa.finance_service.wallets.domain.Wallet;

import jakarta.validation.Valid;

public interface CreateWalletInputPort {
    public Wallet handle(@Valid CreateWalletDTO createWalletDTO);
}
