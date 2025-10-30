package com.sa.finance_service.wallets.application.usecases;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.sa.finance_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finance_service.payments.application.dtos.TransactionableType;
import com.sa.finance_service.payments.application.inputports.CreatePaymentInputPort;
import com.sa.finance_service.wallets.application.inputport.RechargeWalletInputPort;
import com.sa.finance_service.wallets.application.outputports.FindWalletByOwnerIdOutputPort;
import com.sa.finance_service.wallets.domain.Wallet;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RechargeWalletUseCase implements RechargeWalletInputPort {

    private final FindWalletByOwnerIdOutputPort findWalletByOwnerIdOutputPort;
    private final CreatePaymentInputPort createPaymentInputPort;

    @Override
    public Wallet handle(UUID ownerId, BigDecimal amount) {
        if (ownerId == null) {
            throw new IllegalArgumentException("El id del propietario es obligatorio");
        }

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        Wallet wallet = findWalletByOwnerIdOutputPort.handle(ownerId);

        CreatePaymentDTO paymentDTO = new CreatePaymentDTO();
        paymentDTO.setSubtotal(amount);
        paymentDTO.setDiscount(BigDecimal.ZERO);
        paymentDTO.setTotal(amount);
        paymentDTO.setOriginType(TransactionableType.RECHARGE);
        paymentDTO.setDestinationId(ownerId);
        paymentDTO.setDestinationName(wallet.getOwnerType().name());
        paymentDTO.setDestinationType(TransactionableType.valueOf(wallet.getOwnerType().name()));

        createPaymentInputPort.handle(paymentDTO);

        return findWalletByOwnerIdOutputPort.handle(ownerId);
    }

}
