package com.sa.finance_service.wallets.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
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

import com.sa.finance_service.wallets.application.dtos.CreateWalletDTO;
import com.sa.finance_service.wallets.application.outputports.SaveWalletOutputPort;
import com.sa.finance_service.wallets.domain.OwnerType;
import com.sa.finance_service.wallets.domain.Wallet;

@ExtendWith(MockitoExtension.class)
class CreateWalletUseCaseTest {

    @Mock
    private SaveWalletOutputPort saveWalletOutputPort;

    @InjectMocks
    private CreateWalletUseCase createWalletUseCase;

    @Test
    void handleShouldPersistWalletWithInitialBalance() {
        UUID ownerId = UUID.randomUUID();
        CreateWalletDTO dto = CreateWalletDTO.builder()
            .ownerId(ownerId)
            .ownerType(OwnerType.CINEMA)
            .build();

        Wallet persistedWallet = new Wallet();
        persistedWallet.setId(UUID.randomUUID());
        persistedWallet.setOwnerId(ownerId);
        persistedWallet.setOwnerType(OwnerType.CINEMA);
        persistedWallet.setBalance(BigDecimal.ZERO);

        when(saveWalletOutputPort.save(any(Wallet.class))).thenReturn(persistedWallet);

        Wallet result = createWalletUseCase.handle(dto);

        assertSame(persistedWallet, result);

        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
        verify(saveWalletOutputPort).save(walletCaptor.capture());
        Wallet walletPersisted = walletCaptor.getValue();

        assertEquals(ownerId, walletPersisted.getOwnerId());
        assertEquals(OwnerType.CINEMA, walletPersisted.getOwnerType());
        assertEquals(BigDecimal.ZERO, walletPersisted.getBalance());
    }
}
