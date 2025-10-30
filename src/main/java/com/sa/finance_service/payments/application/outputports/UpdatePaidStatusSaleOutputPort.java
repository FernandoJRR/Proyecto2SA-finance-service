package com.sa.finance_service.payments.application.outputports;

import java.util.UUID;

public interface UpdatePaidStatusSaleOutputPort {

    public void sendUpdatePaidSaleEvent(UUID saleId, Boolean paid, String message);
}
