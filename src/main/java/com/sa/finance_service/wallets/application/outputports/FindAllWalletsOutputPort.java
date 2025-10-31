package com.sa.finance_service.wallets.application.outputports;

import java.util.List;

import com.sa.finance_service.wallets.domain.Wallet;

public interface FindAllWalletsOutputPort {
    List<Wallet> handle();
}
