package com.sa.finance_service.wallets.application.usecases;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.finance_service.wallets.application.outputports.FindAllWalletsOutputPort;
import com.sa.finance_service.wallets.domain.Wallet;

@ExtendWith(MockitoExtension.class)
class FindAllWalletsUseCaseTest {

    @Mock
    private FindAllWalletsOutputPort findAllWalletsOutputPort;

    @InjectMocks
    private FindAllWalletsUseCase findAllWalletsUseCase;

    @Test
    void handleShouldDelegateToOutputPort() {
        List<Wallet> wallets = List.of(new Wallet(), new Wallet());
        when(findAllWalletsOutputPort.handle()).thenReturn(wallets);

        List<Wallet> result = findAllWalletsUseCase.handle();

        assertSame(wallets, result);
        verify(findAllWalletsOutputPort).handle();
    }
}
