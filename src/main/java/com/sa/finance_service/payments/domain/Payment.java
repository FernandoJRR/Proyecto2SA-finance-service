package com.sa.finance_service.payments.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Payment {
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;

    private LocalDate paidAt;
}
