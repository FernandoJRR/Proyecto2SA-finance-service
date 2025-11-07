package com.sa.finance_service.payments.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import com.sa.finance_service.payments.application.outputports.SavePaymentOutputPort;
import com.sa.finance_service.payments.domain.Payment;
import com.sa.finance_service.wallets.application.outputports.FindWalletByOwnerIdOutputPort;
import com.sa.finance_service.wallets.application.outputports.SaveWalletOutputPort;
import com.sa.finance_service.wallets.domain.OwnerType;
import com.sa.finance_service.wallets.domain.Wallet;

@ExtendWith(MockitoExtension.class)
class CreatePaymentUseCaseTest {

    @Mock
    private SaveWalletOutputPort saveWalletOutputPort;

    @Mock
    private SavePaymentOutputPort savePaymentOutputPort;

    @Mock
    private FindWalletByOwnerIdOutputPort findWalletByOwnerIdOutputPort;

    @InjectMocks
    private CreatePaymentUseCase createPaymentUseCase;

    @Test
    void handleShouldUpdateOnlyDestinationWhenRecharge() {
        UUID destinationId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(200);

        CreatePaymentDTO dto = new CreatePaymentDTO(
            amount,
            BigDecimal.ZERO,
            amount,
            null,
            null,
            TransactionableType.RECHARGE,
            destinationId,
            OwnerType.CUSTOMER.name(),
            TransactionableType.CUSTOMER
        );

        Wallet destinationWallet = wallet(destinationId, OwnerType.CUSTOMER, BigDecimal.valueOf(50));
        when(findWalletByOwnerIdOutputPort.handle(destinationId)).thenReturn(destinationWallet);
        when(savePaymentOutputPort.handle(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = createPaymentUseCase.handle(dto);

        assertEquals(amount, result.getTotal());

        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
        verify(saveWalletOutputPort).save(walletCaptor.capture());
        Wallet savedDestination = walletCaptor.getValue();
        assertEquals(BigDecimal.valueOf(250), savedDestination.getBalance());
        assertEquals(destinationId, savedDestination.getOwnerId());

        verify(findWalletByOwnerIdOutputPort, times(1)).handle(destinationId);
        verify(savePaymentOutputPort).handle(any(Payment.class));
    }

    @Test
    void handleShouldUpdateBothWalletsForSaleTransactions() {
        UUID originId = UUID.randomUUID();
        UUID destinationId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(75);

        CreatePaymentDTO dto = new CreatePaymentDTO(
            amount,
            BigDecimal.ZERO,
            amount,
            originId,
            "Cinema SA",
            TransactionableType.CINEMA,
            destinationId,
            "Customer",
            TransactionableType.CUSTOMER
        );

        Wallet originWallet = wallet(originId, OwnerType.CINEMA, BigDecimal.valueOf(300));
        Wallet destinationWallet = wallet(destinationId, OwnerType.CUSTOMER, BigDecimal.valueOf(25));

        when(findWalletByOwnerIdOutputPort.handle(originId)).thenReturn(originWallet);
        when(findWalletByOwnerIdOutputPort.handle(destinationId)).thenReturn(destinationWallet);
        when(savePaymentOutputPort.handle(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        createPaymentUseCase.handle(dto);

        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
        verify(saveWalletOutputPort, times(2)).save(walletCaptor.capture());
        Wallet savedOrigin = walletCaptor.getAllValues().get(0);
        Wallet savedDestination = walletCaptor.getAllValues().get(1);

        assertEquals(BigDecimal.valueOf(225), savedOrigin.getBalance());
        assertEquals(originId, savedOrigin.getOwnerId());

        assertEquals(BigDecimal.valueOf(100), savedDestination.getBalance());
        assertEquals(destinationId, savedDestination.getOwnerId());

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(savePaymentOutputPort).handle(paymentCaptor.capture());
        Payment persistedPayment = paymentCaptor.getValue();
        assertEquals(amount, persistedPayment.getTotal());
        assertEquals(originId, persistedPayment.getOriginId());
        assertEquals(destinationId, persistedPayment.getDestinationId());
    }

    @Test
    void handleShouldValidateOriginIdWhenNotRecharge() {
        CreatePaymentDTO dto = new CreatePaymentDTO(
            BigDecimal.TEN,
            BigDecimal.ZERO,
            BigDecimal.TEN,
            null,
            "Cinema SA",
            TransactionableType.CINEMA,
            UUID.randomUUID(),
            "Customer",
            TransactionableType.CUSTOMER
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> createPaymentUseCase.handle(dto));
        assertEquals("El id de origen es obligatorio", exception.getMessage());
    }

    @Test
    void handleShouldValidateOriginNameWhenNotRecharge() {
        CreatePaymentDTO dto = new CreatePaymentDTO(
            BigDecimal.TEN,
            BigDecimal.ZERO,
            BigDecimal.TEN,
            UUID.randomUUID(),
            null,
            TransactionableType.CINEMA,
            UUID.randomUUID(),
            "Customer",
            TransactionableType.CUSTOMER
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> createPaymentUseCase.handle(dto));
        assertEquals("El nombre de origen es obligatorio", exception.getMessage());
    }

    private Wallet wallet(UUID ownerId, OwnerType ownerType, BigDecimal balance) {
        Wallet wallet = new Wallet();
        wallet.setOwnerId(ownerId);
        wallet.setOwnerType(ownerType);
        wallet.setBalance(balance);
        return wallet;
    }
}
