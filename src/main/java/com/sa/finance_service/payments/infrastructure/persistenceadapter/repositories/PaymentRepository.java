package com.sa.finance_service.payments.infrastructure.persistenceadapter.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.finance_service.payments.infrastructure.persistenceadapter.models.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
    public Optional<PaymentEntity> findOneById(UUID id);
}
