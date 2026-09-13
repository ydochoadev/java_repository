package com.atlas.bank.atlas.transaction.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DifferentAccountValidator.class)
public @interface DifferentAccount {
    String message() default "La cuenta de origin y destino no pueden ser la misma";

    // Parte del contrato Validation
    Class<?>[] groups() default {}; // Agrupa validaciones para ejecutar solo algunas según contexto

    Class<? extends Payload>[] payload() default {}; // Clasifica la severidad del error
}

