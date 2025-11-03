package com.sa.finance_service.payments.infrastructure.persistenceadapter.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.sa.finance_service.payments.application.dtos.TransactionableType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "subtotal", nullable = false, precision = 19, scale = 4)
    private BigDecimal subtotal;

    @Column(name = "discount", nullable = false, precision = 19, scale = 4)
    private BigDecimal discount;

    @Column(name = "total", nullable = false, precision = 19, scale = 4)
    private BigDecimal total;

    @Column(name = "origin_id", nullable = true)
    private UUID originId;

    @Column(name = "origin_name", nullable = false, length = 255)
    private String originName;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin_type", nullable = false)
    private TransactionableType originType;

    @Column(name = "destination_id", nullable = false)
    private UUID destinationId;

    @Column(name = "destination_name", nullable = false, length = 255)
    private String destinationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "destination_type", nullable = false)
    private TransactionableType destinationType;

    @Column(name = "paid_at", nullable = false)
    private LocalDate paidAt;
}
