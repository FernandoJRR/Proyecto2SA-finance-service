package com.sa.finance_service.payments.infrastructure.kafkaadapter.listeners;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.finance_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finance_service.payments.application.inputports.CreatePaymentInputPort;
import com.sa.finance_service.payments.infrastructure.kafkaadapter.adapters.UpdatePaidStatusAddAdapter;
import com.sap.common_lib.dto.response.add.events.AddPendingPaymentEventDTO;

@ExtendWith(MockitoExtension.class)
class AddsPendingPaymentListenerTest {

    @Mock
    private CreatePaymentInputPort createPaymentInputPort;

    @Mock
    private UpdatePaidStatusAddAdapter updatePaidStatusAddAdapter;

    @InjectMocks
    private AddsPendingPaymentListener addsPendingPaymentListener;

    private static final UUID ADD_ID = UUID.randomUUID();
    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID CINEMA_ID = UUID.randomUUID();
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(150.00);

    /**
     * dado: un evento AddPendingPaymentEventDTO válido
     * cuando: se procesa correctamente
     * entonces: se crea el pago y se marca el anuncio como pagado
     */
    @Test
    void onAddsPendingPaymentCreatesPaymentAndUpdatesAdSuccessfully() {
        // Arrange
        AddPendingPaymentEventDTO event = new AddPendingPaymentEventDTO(ADD_ID, USER_ID, CINEMA_ID, AMOUNT);

        // Act
        addsPendingPaymentListener.onAddsPendingPayment(event);

        // Assert
        assertAll(
                () -> verify(createPaymentInputPort, times(1))
                        .handle(argThat(dto -> dto.getSubtotal().equals(AMOUNT)
                                && dto.getTotal().equals(AMOUNT)
                                && dto.getOriginId().equals(USER_ID)
                                && dto.getDestinationId().equals(CINEMA_ID))),
                () -> verify(updatePaidStatusAddAdapter, times(1))
                        .sendUpdatePaidAdEvent(ADD_ID, true, "Pago exitoso"));
    }

    /**
     * dado: ocurre una excepción durante la creación del pago
     * cuando: se procesa el evento
     * entonces: se envía evento de pago fallido y se lanza IllegalStateException
     */
    @Test
    void onAddsPendingPaymentThrowsWhenPaymentCreationFails() {
        // Arrange
        AddPendingPaymentEventDTO event = new AddPendingPaymentEventDTO(ADD_ID, USER_ID, CINEMA_ID, AMOUNT);
        doThrow(new RuntimeException("DB error")).when(createPaymentInputPort).handle(any(CreatePaymentDTO.class));

        // Act + Assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> addsPendingPaymentListener.onAddsPendingPayment(event));

        assertTrue(ex.getMessage().contains("Error al procesar la peticion del pago"));
        verify(createPaymentInputPort).handle(any(CreatePaymentDTO.class));
        verify(updatePaidStatusAddAdapter)
                .sendUpdatePaidAdEvent(eq(ADD_ID), eq(false), contains("Pago fallido"));
    }
}
