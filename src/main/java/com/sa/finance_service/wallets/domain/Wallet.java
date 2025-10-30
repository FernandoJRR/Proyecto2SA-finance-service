package com.sa.finance_service.wallets.domain;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Wallet {
    private UUID id;
    private BigDecimal balance;
    private UUID ownerId;
    private OwnerType ownerType;
}
