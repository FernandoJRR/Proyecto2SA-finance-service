package com.sa.finance_service.payments.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatePaymentDTO {
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;

    private UUID originId;
    private String originName;
    private TransactionableType originType;
    private UUID destinationId;
    private String destinationName;
    private TransactionableType destinationType;
}
