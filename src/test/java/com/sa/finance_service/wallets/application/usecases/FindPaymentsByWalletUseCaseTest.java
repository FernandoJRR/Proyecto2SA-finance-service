package com.sa.finance_service.wallets.application.usecases;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.finance_service.payments.application.outputports.FindPaymentsByWalletOutputPort;
import com.sa.finance_service.payments.domain.Payment;

@ExtendWith(MockitoExtension.class)
class FindPaymentsByWalletUseCaseTest {

    @Mock
    private FindPaymentsByWalletOutputPort findPaymentsByWalletOutputPort;

    @InjectMocks
    private FindPaymentsByWalletUseCase findPaymentsByWalletUseCase;

    @Test
    void handleShouldFailWhenWalletIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> findPaymentsByWalletUseCase.handle(null));
    }

    @Test
    void handleShouldReturnPaymentsFromOutputPort() {
        UUID walletId = UUID.randomUUID();
        List<Payment> payments = List.of(mock(Payment.class));
        when(findPaymentsByWalletOutputPort.handle(walletId)).thenReturn(payments);

        List<Payment> result = findPaymentsByWalletUseCase.handle(walletId);

        assertSame(payments, result);
        verify(findPaymentsByWalletOutputPort).handle(walletId);
    }
}
