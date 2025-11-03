package com.sa.finance_service.wallets.infrastructure.restadapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sa.finance_service.wallets.application.dtos.CreateWalletDTO;
import com.sa.finance_service.wallets.application.inputport.CreateWalletInputPort;
import com.sa.finance_service.wallets.application.inputport.FindAllWalletsInputPort;
import com.sa.finance_service.wallets.application.inputport.FindWalletByOwnerIdInputPort;
import com.sa.finance_service.wallets.application.inputport.RechargeWalletInputPort;
import com.sa.finance_service.wallets.domain.Wallet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(path = "/api/v1/wallets", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Carteras", description = "Operaciones para la gestión de carteras")
public class WalletController {

    private final CreateWalletInputPort createWalletInputPort;
    private final FindAllWalletsInputPort findAllWalletsInputPort;
    private final FindWalletByOwnerIdInputPort findWalletByOwnerIdInputPort;
    private final RechargeWalletInputPort rechargeWalletInputPort;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Crear una cartera",
        description = "Registra una nueva cartera para el propietario indicado con un saldo inicial en cero."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cartera creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT') or hasRole('CINEMA_ADMIN') or hasRole('SPONSOR')")
    public Wallet createWallet(@Valid @RequestBody CreateWalletDTO createWalletDTO) {
        return createWalletInputPort.handle(createWalletDTO);
    }

    @GetMapping
    @Operation(
        summary = "Listar todas las carteras",
        description = "Devuelve la colección completa de carteras registradas."
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Wallet> findAll() {
        return findAllWalletsInputPort.handle();
    }

    @GetMapping(path = "/{ownerId}")
    @Operation(
        summary = "Consultar cartera por propietario",
        description = "Obtiene la información de la cartera asociada al identificador del propietario."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cartera encontrada"),
        @ApiResponse(responseCode = "404", description = "No existe una cartera para el propietario indicado")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT') or hasRole('CINEMA_ADMIN') or hasRole('SPONSOR')")
    public Wallet findByOwnerId(
        @Parameter(description = "Identificador del propietario de la cartera", required = true)
        @PathVariable UUID ownerId
    ) {
        return findWalletByOwnerIdInputPort.handle(ownerId);
    }

    @PostMapping(path = "/{ownerId}/recharge", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Recargar saldo de la cartera",
        description = "Solicita un pago de tipo recarga para aumentar el saldo de la cartera del propietario."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Recarga procesada correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "404", description = "No existe una cartera para el propietario indicado")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT') or hasRole('CINEMA_ADMIN') or hasRole('SPONSOR')")
    public Wallet rechargeWallet(
        @Parameter(description = "Identificador del propietario de la cartera", required = true)
        @PathVariable UUID ownerId,
        @Valid @RequestBody RechargeWalletRequest request
    ) {
        return rechargeWalletInputPort.handle(ownerId, request.amount());
    }

    @Schema(description = "Petición para recargar el saldo de una cartera")
    public record RechargeWalletRequest(
        @NotNull(message = "El monto de recarga es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
        BigDecimal amount
    ) { }
}
