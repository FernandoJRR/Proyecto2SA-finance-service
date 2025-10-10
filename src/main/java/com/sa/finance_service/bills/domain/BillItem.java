package com.sa.finance_service.bills.domain;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BillItem {
    private String description;
    private BigDecimal value;
    private Integer amount;
}
