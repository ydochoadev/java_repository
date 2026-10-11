package com.atlas.bank.atlas.application.service;

import com.atlas.bank.atlas.application.port.out.TransactionRepositoryPort;
import com.atlas.bank.atlas.domain.model.transaction.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
public abstract class TransactionProcessor<C> {

    // Inyecta el repo porque en cada operación (retiro, trx, depósito) necesita la BD
    // Para no colocarlo en cada servicio
    protected final TransactionRepositoryPort transactionRepository;

    @Transactional
    public Transaction process(C context) {
        validate(context);
        BigDecimal fee = calculateFee(context);
        execute(context, fee);
        return save(context, fee);
    }

    protected abstract void validate(C context);

    protected abstract BigDecimal calculateFee(C context);

    protected abstract void execute(C context, BigDecimal fee);

    protected abstract Transaction save(C context, BigDecimal fee);
}
