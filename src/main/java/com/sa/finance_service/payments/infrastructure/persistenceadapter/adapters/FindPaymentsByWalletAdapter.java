package com.sa.finance_service.payments.infrastructure.persistenceadapter.adapters;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.sa.finance_service.payments.application.outputports.FindPaymentsByWalletOutputPort;
import com.sa.finance_service.payments.domain.Payment;
import com.sa.finance_service.payments.infrastructure.persistenceadapter.mappers.PaymentRepositoryMapper;
import com.sa.finance_service.payments.infrastructure.persistenceadapter.repositories.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FindPaymentsByWalletAdapter implements FindPaymentsByWalletOutputPort {

    private final PaymentRepository paymentRepository;

    @Override
    public List<Payment> handle(UUID walletId) {
        return paymentRepository.findByOriginIdOrDestinationId(walletId, walletId)
            .stream()
            .map(PaymentRepositoryMapper.INSTANCE::toDomain)
            .toList();
    }
}
