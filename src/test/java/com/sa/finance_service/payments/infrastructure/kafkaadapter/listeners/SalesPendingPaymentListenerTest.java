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
import com.sa.finance_service.payments.infrastructure.kafkaadapter.adapters.UpdatePaidStatusSaleAdapter;
import com.sap.common_lib.dto.response.sales.events.PaidPendingSaleEventDTO;

@ExtendWith(MockitoExtension.class)
class SalesPendingPaymentListenerTest {

    @Mock
    private CreatePaymentInputPort createPaymentInputPort;

    @Mock
    private UpdatePaidStatusSaleAdapter updatePaidStatusSaleAdapter;

    @InjectMocks
    private SalesPendingPaymentListener salesPendingPaymentListener;

    private static final UUID SALE_ID = UUID.randomUUID();
    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final UUID CINEMA_ID = UUID.randomUUID();
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(250.00);

    /**
     * dado: un evento PaidPendingSaleEventDTO válido
     * cuando: se procesa correctamente
     * entonces: se crea el pago y se marca la venta como pagada
     */
    @Test
    void onAddsPendingPaymentCreatesPaymentAndUpdatesSaleSuccessfully() {
        // Arrange
        PaidPendingSaleEventDTO event = new PaidPendingSaleEventDTO(CUSTOMER_ID, CINEMA_ID, SALE_ID, AMOUNT);

        // Act
        salesPendingPaymentListener.onAddsPendingPayment(event);

        // Assert
        assertAll(
                () -> verify(createPaymentInputPort, times(1))
                        .handle(argThat(dto -> dto.getSubtotal().equals(AMOUNT)
                                && dto.getTotal().equals(AMOUNT)
                                && dto.getOriginId().equals(CUSTOMER_ID)
                                && dto.getDestinationId().equals(CINEMA_ID))),
                () -> verify(updatePaidStatusSaleAdapter, times(1))
                        .sendUpdatePaidSaleEvent(SALE_ID, true, "Pago exitoso"));
    }

    /**
     * dado: ocurre una excepción durante la creación del pago
     * cuando: se procesa el evento
     * entonces: se envía evento de pago fallido y se lanza IllegalStateException
     */
    @Test
    void onAddsPendingPaymentThrowsWhenPaymentCreationFails() {
        // Arrange
        PaidPendingSaleEventDTO event = new PaidPendingSaleEventDTO(CUSTOMER_ID, CINEMA_ID, SALE_ID, AMOUNT);
        doThrow(new RuntimeException("DB error")).when(createPaymentInputPort).handle(any(CreatePaymentDTO.class));

        // Act + Assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> salesPendingPaymentListener.onAddsPendingPayment(event));

        assertTrue(ex.getMessage().contains("Error al procesar la peticion del pago"));
        verify(createPaymentInputPort).handle(any(CreatePaymentDTO.class));
        verify(updatePaidStatusSaleAdapter).sendUpdatePaidSaleEvent(eq(SALE_ID), eq(false), contains("Pago fallido"));
    }
}
