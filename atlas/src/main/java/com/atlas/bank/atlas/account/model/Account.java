package com.atlas.bank.atlas.account.model;

import com.atlas.bank.atlas.shared.model.Currency;
import com.atlas.bank.atlas.shared.model.Email;
import com.atlas.bank.atlas.shared.model.Money;
import com.atlas.bank.atlas.transaction.exeption.InsufficientFundsException;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Embedded;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false))
            }
    )
    private Email email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType type; // SAVING, CHECKING

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "amount", column = @Column(name = "balance", nullable = false)),
                    @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false, length = 3))
            }
    )
    private Money balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status; // ACTIVE, CLOSED, FROZEN

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "customer_id")
    private Long customerId;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = AccountStatus.ACTIVE;
        if (this.balance == null) this.balance = Money.zero(Currency.ARS);
    }

    public void deposit(Money money) {
        if (money.isNegative()) {
            throw new IllegalArgumentException("El monto a depositar no puede ser negativo");
        }
        this.balance = this.balance.add(money);
    }

    public void withdraw(Money money) {
        if (money.isNegative()) {
            throw new IllegalArgumentException("El monto a retirar no puede ser negativo");
        }
        if (this.balance.isLessThan(money)) {
            throw new InsufficientFundsException(id, this.balance.getAmount(), money.getAmount());
        }

        this.balance = this.balance.subtract(money);
    }
}
