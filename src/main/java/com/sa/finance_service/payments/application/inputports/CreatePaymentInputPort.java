package com.sa.finance_service.payments.application.inputports;

import com.sa.finance_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finance_service.payments.domain.Payment;

import jakarta.validation.Valid;

public interface CreatePaymentInputPort {
    public Payment handle(@Valid CreatePaymentDTO createPaymentDTO);
}
