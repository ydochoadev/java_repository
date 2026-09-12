package com.atlas.bank.atlas.account.dto;

import lombok.Data;

import java.math.BigDecimal;

// @Data => Genera getter, setter, toString, equalsAndHashcode, RequiredArgumentConstrictor
// problema en las entity que tienen relaciones: Data usado en DTO no es problemático
@Data
public class CreateAccountRequest {
    private String accountNumber;
    private String ownerName;
    private String email;
    private String type;
    private BigDecimal balance;
}
