package com.sa.finance_service.payments.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Data
public class Payment {
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;

    private LocalDate paidAt;

    public static Payment create(BigDecimal subtotal, BigDecimal discount, BigDecimal total){
        return new Payment(subtotal, discount, total, LocalDate.now());
    }
}
