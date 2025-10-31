package com.sa.finance_service.wallets.application.usecases;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sa.finance_service.wallets.application.inputport.FindAllWalletsInputPort;
import com.sa.finance_service.wallets.application.outputports.FindAllWalletsOutputPort;
import com.sa.finance_service.wallets.domain.Wallet;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FindAllWalletsUseCase implements FindAllWalletsInputPort {

    private final FindAllWalletsOutputPort findAllWalletsOutputPort;

    @Override
    public List<Wallet> handle() {
        return findAllWalletsOutputPort.handle();
    }

}
