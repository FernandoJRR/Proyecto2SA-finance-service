package com.sa.finance_service.wallets.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.sa.finance_service.payments.application.outputports.FindPaymentsByWalletOutputPort;
import com.sa.finance_service.payments.domain.Payment;
import com.sa.finance_service.wallets.application.inputport.FindPaymentsByWalletInputPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FindPaymentsByWalletUseCase implements FindPaymentsByWalletInputPort {

    private final FindPaymentsByWalletOutputPort findPaymentsByWalletOutputPort;

    @Override
    public List<Payment> handle(UUID walletId) {
        if (walletId == null) {
            throw new IllegalArgumentException("El identificador de la cartera es obligatorio");
        }

        return findPaymentsByWalletOutputPort.handle(walletId);
    }

}
