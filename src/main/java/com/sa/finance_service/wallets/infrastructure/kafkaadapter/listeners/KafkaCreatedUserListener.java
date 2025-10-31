package com.sa.finance_service.wallets.infrastructure.kafkaadapter.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.sa.finance_service.wallets.application.dtos.CreateWalletDTO;
import com.sa.finance_service.wallets.application.inputport.CreateWalletInputPort;
import com.sa.finance_service.wallets.domain.OwnerType;
import com.sap.common_lib.dto.response.users.event.UserCreatedEventDTO;
import com.sap.common_lib.events.groups.GroupsConstants;
import com.sap.common_lib.events.topics.TopicConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaCreatedUserListener {

    private final CreateWalletInputPort createWalletInputPort;

    @KafkaListener(
        topics = TopicConstants.USER_CREATED,
        groupId = GroupsConstants.USER_SERVICE_GROUP_ID
    )
    public void onCreatedUser(@Payload UserCreatedEventDTO message) {
        try {
            CreateWalletDTO createWalletDTO = new CreateWalletDTO(message.getUserId(), OwnerType.CUSTOMER);

            createWalletInputPort.handle(createWalletDTO);
            log.info(
                "Cartera creada exitosamente para usuario {}",
                message.getUserId());
        } catch (Exception exception) {
            log.error(
                "No se pudo crear la cartera para el usuario por: {}",
                message,
                exception);
            throw new IllegalStateException("Error al procesar la peticion de la cartera", exception);
        }
    }
}
