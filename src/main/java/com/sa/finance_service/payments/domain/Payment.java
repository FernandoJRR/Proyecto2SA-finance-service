package com.sa.finance_service.payments.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.sa.finance_service.payments.application.dtos.TransactionableType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Data
public class Payment {
    private UUID id;

    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;

    private UUID originId;
    private String originName;
    private TransactionableType originType;
    private UUID destinationId;
    private String destinationName;
    private TransactionableType destinationType;

    private LocalDate paidAt;

    public static Payment create(BigDecimal subtotal, BigDecimal discount, BigDecimal total,
            UUID originId, String originName,
            TransactionableType originType,
            UUID destinationId,
            String destinationName,
            TransactionableType destinationType
    ){
        return new Payment(UUID.randomUUID(), subtotal, discount, total,
                originId, originName, originType,
                destinationId, destinationName, destinationType,
                LocalDate.now());
    }
}
