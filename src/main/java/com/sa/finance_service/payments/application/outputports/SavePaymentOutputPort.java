package com.sa.finance_service.payments.application.outputports;

import com.sa.finance_service.payments.domain.Payment;

public interface SavePaymentOutputPort {
    public Payment handle(Payment payment);
}
