package com.atlas.bank.atlas.shared.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode // Comparar por valor
public class Money {

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency currency;

    // Nadie fuera de la clase pueda realizar un new (usar factory method)
    private Money(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP); // redondeo hacia arriba
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return Money.of(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        return Money.of(this.amount.subtract(other.amount), this.currency);
    }

    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }

    private void validateSameCurrency(Money money) {
        if (this.currency != money.currency) {
            throw new IllegalArgumentException("No se pueden operar montos en monedas distintas: " + this.currency + " VS " + money.currency);
        }
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                '}';
    }
}
