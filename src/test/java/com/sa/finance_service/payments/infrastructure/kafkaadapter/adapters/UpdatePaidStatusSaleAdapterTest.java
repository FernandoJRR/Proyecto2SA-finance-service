package com.sa.finance_service.payments.infrastructure.kafkaadapter.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.sap.common_lib.dto.response.sales.events.UpdateStateSaleEventDTO;
import com.sap.common_lib.events.topics.TopicConstants;

@ExtendWith(MockitoExtension.class)
class UpdatePaidStatusSaleAdapterTest {

    @Mock
    private KafkaTemplate<String, UpdateStateSaleEventDTO> kafkaTemplate;

    @InjectMocks
    private UpdatePaidStatusSaleAdapter updatePaidStatusSaleAdapter;

    private static final UUID SALE_ID = UUID.randomUUID();
    private static final boolean PAID = true;
    private static final String MESSAGE = "Pago confirmado";

    /**
     * dado: un saleId, un estado de pago y un mensaje válidos
     * cuando: se envía el evento de actualización
     * entonces: se invoca KafkaTemplate con los valores esperados
     */
    @Test
    void sendUpdatePaidSaleEventPublishesKafkaMessage() {
        // Arrange
        UpdateStateSaleEventDTO expectedEvent =
                new UpdateStateSaleEventDTO(SALE_ID, PAID, MESSAGE);

        // Act
        updatePaidStatusSaleAdapter.sendUpdatePaidSaleEvent(SALE_ID, PAID, MESSAGE);

        // Assert
        assertAll(
            () -> verify(kafkaTemplate, times(1))
                    .send(TopicConstants.UPDATE_PAID_STATUS_SALE_TOPIC, SALE_ID.toString(), expectedEvent)
        );
    }

    /**
     * dado: un saleId nulo
     * cuando: se intenta enviar el evento
     * entonces: lanza NullPointerException
     */
    @Test
    void sendUpdatePaidSaleEventThrowsWhenSaleIdIsNull() {
        // Arrange + Act + Assert
        assertThrows(
            NullPointerException.class,
            () -> updatePaidStatusSaleAdapter.sendUpdatePaidSaleEvent(null, PAID, MESSAGE)
        );
        verifyNoInteractions(kafkaTemplate);
    }
}
