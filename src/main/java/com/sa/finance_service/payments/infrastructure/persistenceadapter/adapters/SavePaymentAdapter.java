package com.sa.finance_service.payments.infrastructure.persistenceadapter.adapters;

import org.springframework.stereotype.Component;

import com.sa.finance_service.payments.application.outputports.SavePaymentOutputPort;
import com.sa.finance_service.payments.domain.Payment;
import com.sa.finance_service.payments.infrastructure.persistenceadapter.mappers.PaymentRepositoryMapper;
import com.sa.finance_service.payments.infrastructure.persistenceadapter.models.PaymentEntity;
import com.sa.finance_service.payments.infrastructure.persistenceadapter.repositories.PaymentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SavePaymentAdapter implements SavePaymentOutputPort {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment handle(Payment payment) {
        PaymentEntity paymentEntity = PaymentRepositoryMapper.INSTANCE.toEntity(payment);
        PaymentEntity savedPayment = paymentRepository.save(paymentEntity);
        return PaymentRepositoryMapper.INSTANCE.toDomain(savedPayment);
    }
}
