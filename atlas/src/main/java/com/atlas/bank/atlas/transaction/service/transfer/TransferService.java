package com.atlas.bank.atlas.transaction.service.transfer;

import com.atlas.bank.atlas.account.exeption.AccountNotFoundException;
import com.atlas.bank.atlas.account.model.Account;
import com.atlas.bank.atlas.transaction.model.Transaction;
import com.atlas.bank.atlas.account.repoditory.AccountRepository;
import com.atlas.bank.atlas.transaction.repository.TransactionRepository;
import com.atlas.bank.atlas.transaction.service.event.TransactionExecutedEvent;
import com.atlas.bank.atlas.transaction.service.factory.TransactionFactory;
import com.atlas.bank.atlas.transaction.service.fee.FeeCalculator;
import com.atlas.bank.atlas.transaction.validation.chain.TransferValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService extends TransactionProcessor<TransferContext> implements ITransferService {

    private final AccountRepository accountRepository;
    private final List<FeeCalculator> feeCalculators; // Se tiene TODA las implementaciones
    private final ApplicationEventPublisher eventPublisher;
    private final List<TransferValidator> validators;

    public TransferService(TransactionRepository transactionRepository,
                           AccountRepository accountRepository,
                           List<FeeCalculator> feeCalculators,
                           ApplicationEventPublisher eventPublisher,
                           List<TransferValidator> validators) {
        super(transactionRepository);
        this.accountRepository = accountRepository;
        this.feeCalculators = feeCalculators;
        this.eventPublisher = eventPublisher;
        this.validators = validators;
    }

    @Override
    @Transactional
    public Transaction execute(Long fromId, Long toId, BigDecimal amount) {
        // Buscar cuentas
        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new AccountNotFoundException(fromId));
        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new AccountNotFoundException(toId));

        // process => aplica el patrón Template Method
        Transaction transaction = process(new TransferContext(from, to, amount));
        // Estados
        transaction.advanceTo(transaction.getState().validate());
        transaction.advanceTo(transaction.getState().execute());
        transactionRepository.save(transaction); // Guarda (actualiza) otra vez la trx con su estado
        // Lanzar el evento
        eventPublisher.publishEvent(new TransactionExecutedEvent(
                transaction.getId(),
                transaction.getType().name(),
                transaction.getSourceAccountId(),
                transaction.getTargetAccountId(),
                transaction.getAmount(),
                transaction.getFee()
        ));
        return transaction;
    }

    @Override
    protected void validate(TransferContext context) {
        // Validar que la cuenta esté activa. Fondos. Y fraude
        validators.forEach(validator -> validator.validate(context));
    }

    @Override
    protected BigDecimal calculateFee(TransferContext context) {
        // Calcular comisión
        return this.feeCalculators.stream()
                .filter(fc -> fc.supports(context.from().getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay calculador para el tipo " + context.from().getType()))
                .calculate(context.amount());
    }

    @Override
    protected void execute(TransferContext context, BigDecimal fee) {
        // Actualizar saldos
        context.from().setBalance(context.from().getBalance().subtract(context.amount()).subtract(fee));
        context.to().setBalance(context.to().getBalance().add(context.amount()));
        accountRepository.save(context.from());
        accountRepository.save(context.to());
    }

    @Override
    protected Transaction save(TransferContext context, BigDecimal fee) {
        // Crear transacción
        Transaction transaction = TransactionFactory.createTransfer(context, fee);

        return transactionRepository.save(transaction);
    }
}
