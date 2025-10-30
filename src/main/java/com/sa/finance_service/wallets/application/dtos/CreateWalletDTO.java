package com.sa.finance_service.wallets.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.sa.finance_service.wallets.domain.OwnerType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletDTO {

    @NotNull
    private UUID ownerId;

    @NotNull
    private OwnerType ownerType;
}
