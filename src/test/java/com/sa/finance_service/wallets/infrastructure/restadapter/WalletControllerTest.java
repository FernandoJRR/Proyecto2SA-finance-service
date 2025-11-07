package com.sa.finance_service.wallets.infrastructure.restadapter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.finance_service.payments.domain.Payment;
import com.sa.finance_service.payments.application.dtos.TransactionableType;
import com.sa.finance_service.wallets.application.dtos.CreateWalletDTO;
import com.sa.finance_service.wallets.application.inputport.CreateWalletInputPort;
import com.sa.finance_service.wallets.application.inputport.FindAllWalletsInputPort;
import com.sa.finance_service.wallets.application.inputport.FindPaymentsByWalletInputPort;
import com.sa.finance_service.wallets.application.inputport.FindWalletByOwnerIdInputPort;
import com.sa.finance_service.wallets.application.inputport.RechargeWalletInputPort;
import com.sa.finance_service.wallets.domain.OwnerType;
import com.sa.finance_service.wallets.domain.Wallet;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Mock
    private CreateWalletInputPort createWalletInputPort;

    @Mock
    private FindAllWalletsInputPort findAllWalletsInputPort;

    @Mock
    private FindWalletByOwnerIdInputPort findWalletByOwnerIdInputPort;

    @Mock
    private FindPaymentsByWalletInputPort findPaymentsByWalletInputPort;

    @Mock
    private RechargeWalletInputPort rechargeWalletInputPort;

    @InjectMocks
    private WalletController walletController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final UUID WALLET_ID = UUID.randomUUID();
    private static final UUID OWNER_ID = UUID.randomUUID();
    private static final BigDecimal BALANCE = BigDecimal.valueOf(500.00);
    private static final OwnerType OWNER_TYPE = OwnerType.CINEMA;
    private static final BigDecimal RECHARGE_AMOUNT = BigDecimal.valueOf(200.00);

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    private Wallet buildWallet() {
        Wallet wallet = new Wallet();
        wallet.setId(WALLET_ID);
        wallet.setBalance(BALANCE);
        wallet.setOwnerId(OWNER_ID);
        wallet.setOwnerType(OWNER_TYPE);
        return wallet;
    }

    /**
     * dado: un CreateWalletDTO válido
     * cuando: se realiza POST /api/v1/wallets
     * entonces: retorna 201 y el wallet creado
     */
    @Test
    void createWalletReturnsCreated() throws Exception {
        // Arrange
        CreateWalletDTO dto = new CreateWalletDTO(OWNER_ID, OWNER_TYPE);
        Wallet expected = buildWallet();
        when(createWalletInputPort.handle(any(CreateWalletDTO.class))).thenReturn(expected);

        // Act + Assert
        mockMvc.perform(post("/api/v1/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(WALLET_ID.toString()))
                .andExpect(jsonPath("$.balance").value(BALANCE.doubleValue()));

        verify(createWalletInputPort).handle(any(CreateWalletDTO.class));
    }

    /**
     * dado: existen wallets en el sistema
     * cuando: se realiza GET /api/v1/wallets
     * entonces: retorna 200 y lista de wallets
     */
    @Test
    void findAllReturnsListOfWallets() throws Exception {
        // Arrange
        when(findAllWalletsInputPort.handle()).thenReturn(List.of(buildWallet()));

        // Act + Assert
        mockMvc.perform(get("/api/v1/wallets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(WALLET_ID.toString()));

        verify(findAllWalletsInputPort).handle();
    }

    /**
     * dado: un ownerId válido
     * cuando: se realiza GET /api/v1/wallets/{ownerId}
     * entonces: retorna 200 y el wallet asociado
     */
    @Test
    void findByOwnerIdReturnsWallet() throws Exception {
        // Arrange
        when(findWalletByOwnerIdInputPort.handle(OWNER_ID)).thenReturn(buildWallet());

        // Act + Assert
        mockMvc.perform(get("/api/v1/wallets/{ownerId}", OWNER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(WALLET_ID.toString()));

        verify(findWalletByOwnerIdInputPort).handle(OWNER_ID);
    }

    /**
     * dado: una wallet existente con pagos
     * cuando: se realiza GET /api/v1/wallets/{walletId}/payments
     * entonces: retorna 200 con lista de pagos
     */
    @Test
    void findPaymentsReturnsListOfPayments() throws Exception {
        // Arrange
        Payment payment = Payment.create(
                BigDecimal.valueOf(100),
                BigDecimal.ZERO,
                BigDecimal.valueOf(100),
                OWNER_ID, "Origen", TransactionableType.CINEMA,
                WALLET_ID, "Destino", TransactionableType.CUSTOMER);
        when(findPaymentsByWalletInputPort.handle(WALLET_ID)).thenReturn(List.of(payment));

        // Act + Assert
        mockMvc.perform(get("/api/v1/wallets/{walletId}/payments", WALLET_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].total").value(100));

        verify(findPaymentsByWalletInputPort).handle(WALLET_ID);
    }

    /**
     * dado: una solicitud de recarga válida
     * cuando: se realiza POST /api/v1/wallets/{ownerId}/recharge
     * entonces: retorna 200 con la cartera actualizada
     */
    @Test
    void rechargeWalletReturnsUpdatedWallet() throws Exception {
        // Arrange
        Wallet updated = buildWallet();
        updated.setBalance(BALANCE.add(RECHARGE_AMOUNT));
        var request = new WalletController.RechargeWalletRequest(RECHARGE_AMOUNT);

        when(rechargeWalletInputPort.handle(eq(OWNER_ID), eq(RECHARGE_AMOUNT)))
                .thenReturn(updated);

        // Act + Assert
        mockMvc.perform(post("/api/v1/wallets/{ownerId}/recharge", OWNER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(updated.getBalance().doubleValue()));

        verify(rechargeWalletInputPort).handle(OWNER_ID, RECHARGE_AMOUNT);
    }
}
