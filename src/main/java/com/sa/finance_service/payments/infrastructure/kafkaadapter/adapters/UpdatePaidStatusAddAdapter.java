package com.sa.finance_service.payments.infrastructure.kafkaadapter.adapters;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.sa.finance_service.payments.application.outputports.UpdatePaidStatusAddOutputPort;
import com.sap.common_lib.dto.response.add.events.ChangePaidStateAddEventDTO;
import com.sap.common_lib.events.topics.TopicConstants;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UpdatePaidStatusAddAdapter implements UpdatePaidStatusAddOutputPort {

    private final KafkaTemplate<String, ChangePaidStateAddEventDTO> changeTemplate;

    @Override
    public void sendUpdatePaidAdEvent(UUID addId, Boolean paid, String message) {
        var eventDTO = new ChangePaidStateAddEventDTO(
                addId,
                paid,
                message
        );
        changeTemplate.send(TopicConstants.UPDATE_PAID_STATUS_ADD_TOPIC, addId.toString(), eventDTO);
    }
}
