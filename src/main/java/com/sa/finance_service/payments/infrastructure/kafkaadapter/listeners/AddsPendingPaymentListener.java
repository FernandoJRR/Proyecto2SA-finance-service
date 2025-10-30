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
import com.sa.finance_service.payments.infrastructure.kafkaadapter.adapters.UpdatePaidStatusAddAdapter;
import com.sap.common_lib.dto.response.add.events.AddPendingPaymentEventDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddsPendingPaymentListener {

    private final CreatePaymentInputPort createPaymentInputPort;
    private final UpdatePaidStatusAddAdapter updatePaidStatusAddAdapter;

    @KafkaListener(
        topics = TopicConstants.ADDS_PENDING_PAYMENT_TOPIC ,
        groupId = GroupsConstants.SALES_SERVICE_GROUP_ID
    )
    public void onAddsPendingPayment(@Payload AddPendingPaymentEventDTO message) {
        try {
            CreatePaymentDTO createPaymentDTO = new CreatePaymentDTO(
                message.amount(),
                BigDecimal.ZERO,
                message.amount(),
                message.userId(),
                "Usuario",
                TransactionableType.CUSTOMER,
                message.cinemaId(),
                "Cine",
                TransactionableType.CINEMA
            );

            createPaymentInputPort.handle(createPaymentDTO);

            updatePaidStatusAddAdapter.sendUpdatePaidAdEvent(message.addId(), true, "Pago exitoso");

            log.info(
                "Pago exitoso para la Ad {}",
                message.addId());
        } catch (Exception exception) {
            updatePaidStatusAddAdapter.sendUpdatePaidAdEvent(message.addId(), false, "Pago fallido por: "+exception);
            log.error(
                "No se pudo crear el pago por: {}",
                message,
                exception);
            throw new IllegalStateException("Error al procesar la peticion del pago", exception);
        }
    }
}
