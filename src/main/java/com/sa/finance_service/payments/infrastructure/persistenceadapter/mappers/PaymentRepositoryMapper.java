package com.sa.finance_service.payments.infrastructure.persistenceadapter.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.sa.finance_service.payments.domain.Payment;
import com.sa.finance_service.payments.infrastructure.persistenceadapter.models.PaymentEntity;

@Mapper
public interface PaymentRepositoryMapper {

    PaymentRepositoryMapper INSTANCE = Mappers.getMapper(PaymentRepositoryMapper.class);

    PaymentEntity toEntity(Payment payment);

    Payment toDomain(PaymentEntity paymentEntity);
}
