package com.sa.finance_service.payments.application.usecases;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.sa.finance_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finance_service.payments.application.dtos.TransactionableType;
import com.sa.finance_service.payments.application.inputports.CreatePaymentInputPort;
import com.sa.finance_service.payments.application.outputports.SavePaymentOutputPort;
import com.sa.finance_service.payments.domain.Payment;
import com.sa.finance_service.wallets.application.outputports.FindWalletByOwnerIdOutputPort;
import com.sa.finance_service.wallets.application.outputports.SaveWalletOutputPort;
import com.sa.finance_service.wallets.domain.Wallet;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Validated
public class CreatePaymentUseCase implements CreatePaymentInputPort {

    private final SaveWalletOutputPort saveWalletOutputPort;
    private final SavePaymentOutputPort savePaymentOutputPort;
    private final FindWalletByOwnerIdOutputPort findByOwnerIdOutputPort;

    @Override
    @Transactional
    public Payment handle(@Valid CreatePaymentDTO createPaymentDTO) {
        Payment payment = Payment.create(
            createPaymentDTO.getSubtotal(),
            createPaymentDTO.getDiscount(),
            createPaymentDTO.getTotal(),
            createPaymentDTO.getOriginId(),
            createPaymentDTO.getOriginName(),
            createPaymentDTO.getOriginType(),
            createPaymentDTO.getDestinationId(),
            createPaymentDTO.getDestinationName(),
            createPaymentDTO.getDestinationType()
        );

        BigDecimal amount = payment.getTotal();

        //If its a recharge there is NO origin wallet
        if (!createPaymentDTO.getOriginType().equals(TransactionableType.RECHARGE)) {
            if (createPaymentDTO.getOriginId() == null) {
                throw new IllegalArgumentException("El id de origen es obligatorio");
            }

            if (createPaymentDTO.getOriginName() == null) {
                throw new IllegalArgumentException("El nombre de origen es obligatorio");
            }

            Wallet originWallet = findByOwnerIdOutputPort.handle(createPaymentDTO.getOriginId());
            originWallet.setBalance(originWallet.getBalance().subtract(amount));
            saveWalletOutputPort.save(originWallet);
        }

        Wallet destinationWallet = findByOwnerIdOutputPort.handle(createPaymentDTO.getDestinationId());
        destinationWallet.setBalance(destinationWallet.getBalance().add(amount));
        saveWalletOutputPort.save(destinationWallet);

        savePaymentOutputPort.handle(payment);

        return payment;
    }
}
