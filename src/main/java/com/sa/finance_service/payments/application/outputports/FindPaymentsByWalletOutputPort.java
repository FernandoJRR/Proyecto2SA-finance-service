package com.sa.finance_service.payments.application.outputports;

import java.util.List;
import java.util.UUID;

import com.sa.finance_service.payments.domain.Payment;

public interface FindPaymentsByWalletOutputPort {
    public List<Payment> handle(UUID walletId);
}
