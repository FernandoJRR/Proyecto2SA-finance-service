package com.sa.finance_service.payments.infrastructure.kafkaadapter.listeners;

import java.math.BigDecimal;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.sap.common_lib.events.groups.GroupsConstants;
import com.sap.common_lib.events.topics.TopicConstants;
import com.sa.finance_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finance_service.payments.application.dtos.TransactionableType;
import com.sa.finance_service.payments.application.inputports.CreatePaymentInputPort;
import com.sa.finance_service.payments.infrastructure.kafkaadapter.adapters.UpdatePaidStatusSaleAdapter;
import com.sap.common_lib.dto.response.add.events.AddPendingPaymentEventDTO;
import com.sap.common_lib.dto.response.sales.events.PaidPendingSaleEventDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SalesPendingPaymentListener {

    private final CreatePaymentInputPort createPaymentInputPort;
    private final UpdatePaidStatusSaleAdapter updatePaidStatusSaleAdapter;

    @KafkaListener(
        topics = TopicConstants.SALES_PENDING_PAYMENT_TOPIC,
        groupId = GroupsConstants.SALES_SERVICE_GROUP_ID
    )
    public void onAddsPendingPayment(@Payload PaidPendingSaleEventDTO message) {
        try {
            CreatePaymentDTO createPaymentDTO = new CreatePaymentDTO(
                message.amount(),
                BigDecimal.ZERO,
                message.amount(),
                message.customerId(),
                "Usuario",
                TransactionableType.CUSTOMER,
                message.cinemaId(),
                "Cine",
                TransactionableType.CINEMA
            );

            createPaymentInputPort.handle(createPaymentDTO);

            updatePaidStatusSaleAdapter.sendUpdatePaidSaleEvent(message.saleId(), true, "Pago exitoso");

            log.info(
                "Pago exitoso para la venta {}",
                message.saleId());
        } catch (Exception exception) {
            updatePaidStatusSaleAdapter.sendUpdatePaidSaleEvent(message.saleId(), false, "Pago fallido por: "+exception);
            log.error(
                "No se pudo crear el pago por: {}",
                message,
                exception);
            throw new IllegalStateException("Error al procesar la peticion del pago", exception);
        }
    }
}
