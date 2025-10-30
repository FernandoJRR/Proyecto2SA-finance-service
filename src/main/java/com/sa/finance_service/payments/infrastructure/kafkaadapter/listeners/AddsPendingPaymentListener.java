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
import com.sap.common_lib.dto.response.add.events.AddPendingPaymentEventDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddsPendingPaymentListener {

    private final CreatePaymentInputPort createPaymentInputPort;

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
            log.info(
                "Cartera creada exitosamente para usuario {}",
                message.userId());
        } catch (Exception exception) {
            log.error(
                "No se pudo crear la cartera para el usuario por: {}",
                message,
                exception);
            throw new IllegalStateException("Error al procesar la peticion de la cartera", exception);
        }
    }
}
