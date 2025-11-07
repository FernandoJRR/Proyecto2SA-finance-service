package com.sa.finance_service.wallets.infrastructure.persistenceadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
class SaveWalletAdapterTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private SaveWalletAdapter saveWalletAdapter;

    private static final UUID WALLET_ID = UUID.randomUUID();
    private static final UUID OWNER_ID = UUID.randomUUID();
    private static final BigDecimal BALANCE = BigDecimal.valueOf(1500.50);
    private static final OwnerType OWNER_TYPE = OwnerType.CINEMA;

    private Wallet buildDomain() {
        Wallet wallet = new Wallet();
        wallet.setId(WALLET_ID);
        wallet.setBalance(BALANCE);
        wallet.setOwnerId(OWNER_ID);
        wallet.setOwnerType(OWNER_TYPE);
        return wallet;
    }

    /**
     * dado: un wallet válido
     * cuando: se guarda usando save()
     * entonces: retorna el wallet guardado correctamente mapeado
     */
    @Test
    void savePersistsAndReturnsMappedWallet() {
        // Arrange
        Wallet wallet = buildDomain();
        WalletEntity entity = WalletRepositoryMapper.INSTANCE.toEntity(wallet);
        when(walletRepository.save(any(WalletEntity.class))).thenReturn(entity);

        // Act
        Wallet result = saveWalletAdapter.save(wallet);

        // Assert
        assertAll(
                () -> assertEquals(wallet.getId(), result.getId()),
                () -> assertEquals(wallet.getBalance(), result.getBalance()),
                () -> assertEquals(wallet.getOwnerId(), result.getOwnerId()),
                () -> assertEquals(wallet.getOwnerType(), result.getOwnerType()));
        verify(walletRepository).save(any(WalletEntity.class));
    }

}
