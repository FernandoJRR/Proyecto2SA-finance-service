package com.sa.finance_service.wallets.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.finance_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finance_service.payments.application.dtos.TransactionableType;
import com.sa.finance_service.payments.application.inputports.CreatePaymentInputPort;
import com.sa.finance_service.payments.domain.Payment;
import com.sa.finance_service.wallets.application.outputports.FindWalletByOwnerIdOutputPort;
import com.sa.finance_service.wallets.domain.OwnerType;
import com.sa.finance_service.wallets.domain.Wallet;

@ExtendWith(MockitoExtension.class)
class RechargeWalletUseCaseTest {

    @Mock
    private FindWalletByOwnerIdOutputPort findWalletByOwnerIdOutputPort;

    @Mock
    private CreatePaymentInputPort createPaymentInputPort;

    @InjectMocks
    private RechargeWalletUseCase rechargeWalletUseCase;

    @Test
    void handleShouldFailWhenOwnerIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rechargeWalletUseCase.handle(null, BigDecimal.TEN));

        assertEquals("El id del propietario es obligatorio", exception.getMessage());
        verifyNoInteractions(findWalletByOwnerIdOutputPort, createPaymentInputPort);
    }

    @Test
    void handleShouldFailWhenAmountIsInvalid() {
        UUID ownerId = UUID.randomUUID();

        IllegalArgumentException nullAmount = assertThrows(IllegalArgumentException.class, () -> rechargeWalletUseCase.handle(ownerId, null));
        assertEquals("El monto debe ser mayor a cero", nullAmount.getMessage());

        IllegalArgumentException nonPositiveAmount = assertThrows(IllegalArgumentException.class, () -> rechargeWalletUseCase.handle(ownerId, BigDecimal.ZERO));
        assertEquals("El monto debe ser mayor a cero", nonPositiveAmount.getMessage());
    }

    @Test
    void handleShouldCreatePaymentAndReturnUpdatedWallet() {
        UUID ownerId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(150);

        Wallet currentWallet = new Wallet();
        currentWallet.setOwnerId(ownerId);
        currentWallet.setOwnerType(OwnerType.CUSTOMER);
        currentWallet.setBalance(BigDecimal.valueOf(350));

        Wallet updatedWallet = new Wallet();
        updatedWallet.setOwnerId(ownerId);
        updatedWallet.setOwnerType(OwnerType.CUSTOMER);
        updatedWallet.setBalance(BigDecimal.valueOf(500));

        when(findWalletByOwnerIdOutputPort.handle(ownerId)).thenReturn(currentWallet, updatedWallet);
        when(createPaymentInputPort.handle(any(CreatePaymentDTO.class))).thenReturn(mock(Payment.class));

        Wallet result = rechargeWalletUseCase.handle(ownerId, amount);

        assertSame(updatedWallet, result);
        verify(findWalletByOwnerIdOutputPort, times(2)).handle(ownerId);

        ArgumentCaptor<CreatePaymentDTO> paymentCaptor = ArgumentCaptor.forClass(CreatePaymentDTO.class);
        verify(createPaymentInputPort).handle(paymentCaptor.capture());
        CreatePaymentDTO paymentDTO = paymentCaptor.getValue();

        assertEquals(amount, paymentDTO.getSubtotal());
        assertEquals(BigDecimal.ZERO, paymentDTO.getDiscount());
        assertEquals(amount, paymentDTO.getTotal());
        assertEquals(TransactionableType.RECHARGE, paymentDTO.getOriginType());
        assertEquals(ownerId, paymentDTO.getDestinationId());
        assertEquals(OwnerType.CUSTOMER.name(), paymentDTO.getDestinationName());
        assertEquals(TransactionableType.CUSTOMER, paymentDTO.getDestinationType());
    }
}
