package com.atlas.bank.atlas.transaction.service;

import com.atlas.bank.atlas.transaction.model.Transaction;
import com.atlas.bank.atlas.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
public abstract class TransactionProcessor<C> {

    // Inyecta el repo porque en cada operación (retiro, trx, depósito) necesita la BD
    // Para no colocarlo en cada servicio
    protected final TransactionRepository transactionRepository;

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
