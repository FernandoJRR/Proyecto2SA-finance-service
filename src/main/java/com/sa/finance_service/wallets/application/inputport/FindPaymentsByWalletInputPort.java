package com.sa.finance_service.wallets.application.inputport;

import java.util.List;
import java.util.UUID;

import com.sa.finance_service.payments.domain.Payment;

public interface FindPaymentsByWalletInputPort {
    public List<Payment> handle(UUID walletId);
}
