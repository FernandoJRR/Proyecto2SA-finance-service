package com.sa.finance_service.payments.application.outputports;

import java.util.UUID;

public interface UpdatePaidStatusAddOutputPort {
    public void sendUpdatePaidAdEvent(UUID addId, Boolean paid, String message);
}
