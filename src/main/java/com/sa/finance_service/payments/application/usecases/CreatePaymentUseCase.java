package com.sa.finance_service.payments.application.usecases;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.sa.finance_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finance_service.payments.application.inputports.CreatePaymentInputPort;
import com.sa.finance_service.payments.application.outputports.SavePaymentOutputPort;
import com.sa.finance_service.payments.domain.Payment;
import com.sa.finance_service.wallets.application.outputports.FindByOwnerIdOutputPort;
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
    private final FindByOwnerIdOutputPort findByOwnerIdOutputPort;

    @Override
    @Transactional
    public Payment handle(@Valid CreatePaymentDTO createPaymentDTO) {
        Payment payment = Payment.create(
            createPaymentDTO.getSubtotal(),
            createPaymentDTO.getDiscount(),
            createPaymentDTO.getTotal()
        );

        Wallet originWallet = findByOwnerIdOutputPort.handle(createPaymentDTO.getOriginId());
        Wallet destinationWallet = findByOwnerIdOutputPort.handle(createPaymentDTO.getDestinationId());

        BigDecimal amount = payment.getTotal();
        originWallet.setBalance(originWallet.getBalance().subtract(amount));
        destinationWallet.setBalance(destinationWallet.getBalance().add(amount));

        saveWalletOutputPort.save(originWallet);
        saveWalletOutputPort.save(destinationWallet);

        savePaymentOutputPort.handle(payment);

        return payment;
    }
}
