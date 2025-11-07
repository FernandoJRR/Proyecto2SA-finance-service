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

import com.sap.common_lib.dto.response.add.events.ChangePaidStateAddEventDTO;
import com.sap.common_lib.events.topics.TopicConstants;

@ExtendWith(MockitoExtension.class)
class UpdatePaidStatusAddAdapterTest {

    @Mock
    private KafkaTemplate<String, ChangePaidStateAddEventDTO> kafkaTemplate;

    @InjectMocks
    private UpdatePaidStatusAddAdapter updatePaidStatusAddAdapter;

    private static final UUID ADD_ID = UUID.randomUUID();
    private static final boolean PAID = true;
    private static final String MESSAGE = "Anuncio pagado correctamente";

    /**
     * dado: un addId, un estado de pago y un mensaje válidos
     * cuando: se envía el evento de actualización de pago de anuncio
     * entonces: se invoca KafkaTemplate con los valores esperados
     */
    @Test
    void sendUpdatePaidAdEventPublishesKafkaMessage() {
        // Arrange
        ChangePaidStateAddEventDTO expectedEvent = new ChangePaidStateAddEventDTO(ADD_ID, PAID, MESSAGE);

        // Act
        updatePaidStatusAddAdapter.sendUpdatePaidAdEvent(ADD_ID, PAID, MESSAGE);

        // Assert
        assertAll(
                () -> verify(kafkaTemplate, times(1))
                        .send(TopicConstants.UPDATE_PAID_STATUS_ADD_TOPIC, ADD_ID.toString(), expectedEvent));
    }

    /**
     * dado: un addId nulo
     * cuando: se intenta enviar el evento
     * entonces: lanza NullPointerException
     */
    @Test
    void sendUpdatePaidAdEventThrowsWhenAddIdIsNull() {
        // Arrange + Act + Assert
        assertThrows(
                NullPointerException.class,
                () -> updatePaidStatusAddAdapter.sendUpdatePaidAdEvent(null, PAID, MESSAGE));
        verifyNoInteractions(kafkaTemplate);
    }
}
