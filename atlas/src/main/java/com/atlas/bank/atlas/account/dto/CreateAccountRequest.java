package com.atlas.bank.atlas.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

// @Data => Genera getter, setter, toString, equalsAndHashcode, RequiredArgumentConstrictor
// problema en las entity que tienen relaciones: Data usado en DTO no es problemático
@Data
public class CreateAccountRequest {
    @NotBlank(message = "El número de cuenta es obligatorio")
    private String accountNumber;

    @NotBlank(message = "El nombre del titular es obligatorio")
    private String ownerName;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String email;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    private String type;

    @PositiveOrZero(message = "El saldo no puede set negativo")
    private BigDecimal balance;
}
