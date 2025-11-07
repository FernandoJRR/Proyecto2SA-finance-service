package com.sa.finance_service.wallets.infrastructure.persistenceadapter.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.finance_service.wallets.domain.OwnerType;
import com.sa.finance_service.wallets.domain.Wallet;
import com.sa.finance_service.wallets.infrastructure.persistenceadapter.mappers.WalletRepositoryMapper;
import com.sa.finance_service.wallets.infrastructure.persistenceadapter.models.WalletEntity;
import com.sa.finance_service.wallets.infrastructure.persistenceadapter.repositories.WalletRepository;

@ExtendWith(MockitoExtension.class)
class FindAllWalletsAdapterTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private FindAllWalletsAdapter findAllWalletsAdapter;

    private static final UUID WALLET_ID = UUID.randomUUID();
    private static final UUID OWNER_ID = UUID.randomUUID();
    private static final BigDecimal BALANCE = BigDecimal.valueOf(500.00);
    private static final OwnerType OWNER_TYPE = OwnerType.CINEMA;

    /**
     * dado: existen registros de wallets
     * cuando: se invoca handle()
     * entonces: retorna lista de wallets mapeados correctamente
     */
    @Test
    void handleReturnsMappedWallets() {
        // Arrange
        WalletEntity entity = WalletEntity.builder()
                .id(WALLET_ID)
                .balance(BALANCE)
                .ownerId(OWNER_ID)
                .ownerType(OWNER_TYPE)
                .build();

        Wallet domain = WalletRepositoryMapper.INSTANCE.toDomain(entity);
        when(walletRepository.findAll()).thenReturn(List.of(entity));

        // Act
        List<Wallet> result = findAllWalletsAdapter.handle();

        // Assert
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals(domain.getId(), result.get(0).getId()),
                () -> assertEquals(domain.getBalance(), result.get(0).getBalance()),
                () -> assertEquals(domain.getOwnerId(), result.get(0).getOwnerId()),
                () -> assertEquals(domain.getOwnerType(), result.get(0).getOwnerType()));
        verify(walletRepository).findAll();
    }

    /**
     * dado: no existen registros de wallets
     * cuando: se invoca handle()
     * entonces: retorna lista vacía
     */
    @Test
    void handleReturnsEmptyListWhenNoWalletsExist() {
        // Arrange
        when(walletRepository.findAll()).thenReturn(List.of());

        // Act
        List<Wallet> result = findAllWalletsAdapter.handle();

        // Assert
        assertTrue(result.isEmpty());
        verify(walletRepository).findAll();
    }
}
