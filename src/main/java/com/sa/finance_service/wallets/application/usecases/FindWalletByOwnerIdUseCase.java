package com.sa.finance_service.wallets.application.usecases;

import java.util.UUID;

import com.sa.finance_service.wallets.application.inputport.FindWalletByOwnerIdInputPort;
import org.springframework.stereotype.Component;

import com.sa.finance_service.wallets.application.outputports.FindWalletByOwnerIdOutputPort;
import com.sap.common_lib.exception.NotFoundException;
import com.sa.finance_service.wallets.domain.Wallet;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FindWalletByOwnerIdUseCase implements FindWalletByOwnerIdInputPort {

    private final FindWalletByOwnerIdOutputPort findWalletByOwnerIdOutputPort;

    @Override
    public Wallet handle(UUID ownerId) throws NotFoundException {
        try {
            return findWalletByOwnerIdOutputPort.handle(ownerId);
        } catch (IllegalArgumentException ex) {
            throw new NotFoundException("No se pudo encontrar una cartera para este propietario");
        }
    }

}
