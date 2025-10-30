package com.sa.finance_service.payments.infrastructure.kafkaadapter.adapters;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.sa.finance_service.payments.application.outputports.UpdatePaidStatusSaleOutputPort;
import com.sap.common_lib.dto.response.sales.events.UpdateStateSaleEventDTO;
import com.sap.common_lib.events.topics.TopicConstants;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UpdatePaidStatusSaleAdapter implements UpdatePaidStatusSaleOutputPort {

    private final KafkaTemplate<String, UpdateStateSaleEventDTO> changeTemplate;

    @Override
    public void sendUpdatePaidSaleEvent(UUID saleId, Boolean paid, String message) {
        var eventDTO = new UpdateStateSaleEventDTO(
                saleId,
                paid,
                message
        );
        changeTemplate.send(TopicConstants.UPDATE_PAID_STATUS_SALE_TOPIC, saleId.toString(), eventDTO);
    }
}
