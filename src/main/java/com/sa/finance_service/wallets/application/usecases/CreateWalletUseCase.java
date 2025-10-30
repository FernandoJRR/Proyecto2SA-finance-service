package com.sa.finance_service.wallets.application.usecases;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.sa.finance_service.wallets.application.dtos.CreateWalletDTO;
import com.sa.finance_service.wallets.application.inputport.CreateWalletInputPort;
import com.sa.finance_service.wallets.application.outputports.SaveWalletOutputPort;
import com.sa.finance_service.wallets.domain.Wallet;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Validated
public class CreateWalletUseCase implements CreateWalletInputPort {

    private final SaveWalletOutputPort saveWalletOutputPort;

    @Override
    public Wallet handle(@Valid CreateWalletDTO createWalletDTO) {
        Wallet wallet = new Wallet();
        wallet.setOwnerId(createWalletDTO.getOwnerId());
        wallet.setOwnerType(createWalletDTO.getOwnerType());
        wallet.setBalance(BigDecimal.ZERO);

        return saveWalletOutputPort.save(wallet);
    }
}
