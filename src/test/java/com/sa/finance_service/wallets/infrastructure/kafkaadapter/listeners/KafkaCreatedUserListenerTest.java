package com.sa.finance_service.wallets.infrastructure.kafkaadapter.listeners;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.finance_service.wallets.application.dtos.CreateWalletDTO;
import com.sa.finance_service.wallets.application.inputport.CreateWalletInputPort;
import com.sa.finance_service.wallets.domain.OwnerType;
import com.sap.common_lib.dto.response.users.event.UserCreatedEventDTO;

@ExtendWith(MockitoExtension.class)
class KafkaCreatedUserListenerTest {

    @Mock
    private CreateWalletInputPort createWalletInputPort;

    @InjectMocks
    private KafkaCreatedUserListener kafkaCreatedUserListener;

    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * dado: un evento UserCreatedEventDTO válido
     * cuando: se recibe el evento
     * entonces: se crea una cartera para el usuario con OwnerType.CUSTOMER
     */
    @Test
    void onCreatedUserCreatesWalletSuccessfully() {
        // Arrange
        UserCreatedEventDTO event = mock(UserCreatedEventDTO.class);
        when(event.getUserId()).thenReturn(USER_ID);

        // Act
        kafkaCreatedUserListener.onCreatedUser(event);

        // Assert
        verify(createWalletInputPort, times(1))
                .handle(argThat(dto -> dto.getOwnerId().equals(USER_ID)
                        && dto.getOwnerType() == OwnerType.CUSTOMER));
    }

    /**
     * dado: ocurre una excepción durante la creación de la cartera
     * cuando: se recibe el evento
     * entonces: lanza IllegalStateException y se registra el error
     */
    @Test
    void onCreatedUserThrowsWhenWalletCreationFails() {
        // Arrange
        UserCreatedEventDTO event = mock(UserCreatedEventDTO.class);
        when(event.getUserId()).thenReturn(USER_ID);
        doThrow(new RuntimeException("DB error")).when(createWalletInputPort).handle(any(CreateWalletDTO.class));

        // Act + Assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> kafkaCreatedUserListener.onCreatedUser(event));

        assertTrue(ex.getMessage().contains("Error al procesar la peticion de la cartera"));
        verify(createWalletInputPort).handle(any(CreateWalletDTO.class));
    }
}
