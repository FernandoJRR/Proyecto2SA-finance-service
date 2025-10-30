package com.sa.finance_service.wallets.infrastructure.persistenceadapter.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.sa.finance_service.wallets.domain.Wallet;
import com.sa.finance_service.wallets.infrastructure.persistenceadapter.models.WalletEntity;

@Mapper
public interface WalletRepositoryMapper {

    WalletRepositoryMapper INSTANCE = Mappers.getMapper(WalletRepositoryMapper.class);

    WalletEntity toEntity(Wallet wallet);

    Wallet toDomain(WalletEntity walletEntity);
}
