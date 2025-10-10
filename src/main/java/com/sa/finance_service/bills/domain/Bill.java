package com.sa.finance_service.bills.domain;

import java.math.BigDecimal;
import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Bill {
    private BigDecimal total;
    private BigDecimal subtotal;
    private BigDecimal discount;

    private ArrayList<BillItem> items;

}
