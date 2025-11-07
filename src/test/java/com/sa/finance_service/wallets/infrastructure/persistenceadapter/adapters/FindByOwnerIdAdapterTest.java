package com.sa.finance_service.wallets.infrastructure.persistenceadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.finance_service.wallets.domain.OwnerType;
import com.sa.finance_service.wallets.domain.Wallet;
import com.sa.finance_service.wallets.infrastructure.persistenceadapter.models.WalletEntity;
import com.sa.finance_service.wallets.infrastructure.persistenceadapter.repositories.WalletRepository;

@ExtendWith(MockitoExtension.class)
class FindByOwnerIdAdapterTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private FindByOwnerIdAdapter findByOwnerIdAdapter;

    private static final UUID WALLET_ID = UUID.randomUUID();
    private static final UUID OWNER_ID = UUID.randomUUID();
    private static final BigDecimal BALANCE = BigDecimal.valueOf(750.25);
    private static final OwnerType OWNER_TYPE = OwnerType.CINEMA;

    private WalletEntity buildEntity() {
        return WalletEntity.builder()
                .id(WALLET_ID)
                .balance(BALANCE)
                .ownerId(OWNER_ID)
                .ownerType(OWNER_TYPE)
                .build();
    }

    /**
     * dado: existe una wallet asociada a un ownerId válido
     * cuando: se invoca handle()
     * entonces: retorna el dominio correctamente mapeado
     */
    @Test
    void handleReturnsWalletWhenOwnerIdExists() {
        // Arrange
        WalletEntity entity = buildEntity();
        when(walletRepository.findByOwnerId(OWNER_ID)).thenReturn(Optional.of(entity));

        // Act
        Wallet result = findByOwnerIdAdapter.handle(OWNER_ID);

        // Assert
        assertAll(
            () -> assertEquals(entity.getId(), result.getId()),
            () -> assertEquals(entity.getBalance(), result.getBalance()),
            () -> assertEquals(entity.getOwnerId(), result.getOwnerId()),
            () -> assertEquals(entity.getOwnerType(), result.getOwnerType())
        );
        verify(walletRepository).findByOwnerId(OWNER_ID);
    }

    /**
     * dado: no existe wallet para el ownerId
     * cuando: se invoca handle()
     * entonces: lanza IllegalArgumentException con mensaje adecuado
     */
    @Test
    void handleThrowsWhenOwnerIdNotFound() {
        // Arrange
        when(walletRepository.findByOwnerId(OWNER_ID)).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> findByOwnerIdAdapter.handle(OWNER_ID)
        );

        assertTrue(ex.getMessage().contains(OWNER_ID.toString()));
        verify(walletRepository).findByOwnerId(OWNER_ID);
    }
}
