package com.atlas.bank.atlas.transaction.model;

import com.atlas.bank.atlas.transaction.model.state.ExecutedState;
import com.atlas.bank.atlas.transaction.model.state.PendingState;
import com.atlas.bank.atlas.transaction.model.state.RejectedState;
import com.atlas.bank.atlas.transaction.model.state.ReversedState;
import com.atlas.bank.atlas.transaction.model.state.TransactionState;
import com.atlas.bank.atlas.transaction.model.state.ValidatedState;
import com.atlas.bank.atlas.transaction.service.event.TransactionExecutedEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Transaction extends AbstractAggregateRoot<Transaction> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TransactionType type; // DEPOSIT, WITHDRAWAL, TRANSFER

    @Column(name = "source_account_id")
    private Long sourceAccountId;

    @Column(name = "target_account_id")
    private Long targetAccountId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal fee;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // PENDING, EXECUTED, REJECTED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Transient
    private TransactionState state;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = TransactionStatus.EXECUTED;
    }

    public TransactionState getState() {
        if (state == null) {
            this.state = switch (this.status) {
                case PENDING -> new PendingState();
                case VALIDATED -> new ValidatedState();
                case EXECUTED -> new ExecutedState();
                case REJECTED -> new RejectedState();
                case REVERSED -> new ReversedState();
            };
        }
        return state;
    }

    public void advanceTo(TransactionState newState) {
        this.state = newState;
        this.status = newState.status();
    }

    public void markAsExecuted() {
        // Solo publica. Cuando se realice un save, spring lo publica
        registerEvent(new TransactionExecutedEvent(this.id,
                this.type.name(),
                this.sourceAccountId,
                this.targetAccountId,
                this.amount,
                this.fee));
    }
}
