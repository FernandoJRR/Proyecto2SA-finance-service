package com.sa.finance_service.wallets.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sap.common_lib.exception.NotFoundException;
import com.sa.finance_service.wallets.application.outputports.FindWalletByOwnerIdOutputPort;
import com.sa.finance_service.wallets.domain.Wallet;

@ExtendWith(MockitoExtension.class)
class FindWalletByOwnerIdUseCaseTest {

    @Mock
    private FindWalletByOwnerIdOutputPort findWalletByOwnerIdOutputPort;

    @InjectMocks
    private FindWalletByOwnerIdUseCase findWalletByOwnerIdUseCase;

    @Test
    void handleShouldReturnWalletWhenFound() {
        UUID ownerId = UUID.randomUUID();
        Wallet wallet = new Wallet();

        when(findWalletByOwnerIdOutputPort.handle(ownerId)).thenReturn(wallet);

        Wallet result = findWalletByOwnerIdUseCase.handle(ownerId);

        assertSame(wallet, result);
        verify(findWalletByOwnerIdOutputPort).handle(ownerId);
    }

    @Test
    void handleShouldTranslateIllegalArgumentToNotFound() {
        UUID ownerId = UUID.randomUUID();
        when(findWalletByOwnerIdOutputPort.handle(ownerId)).thenThrow(new IllegalArgumentException("not found"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> findWalletByOwnerIdUseCase.handle(ownerId));

        assertEquals("No se pudo encontrar una cartera para este propietario", exception.getMessage());
    }
}
