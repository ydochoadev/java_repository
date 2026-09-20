package com.atlas.bank.atlas.transaction.service.transfer;

import com.atlas.bank.atlas.account.exeption.AccountNotFoundException;
import com.atlas.bank.atlas.account.model.Account;
import com.atlas.bank.atlas.transaction.exeption.AccountNotActiveException;
import com.atlas.bank.atlas.transaction.exeption.InsufficientFundsException;
import com.atlas.bank.atlas.transaction.model.Transaction;
import com.atlas.bank.atlas.account.repoditory.AccountRepository;
import com.atlas.bank.atlas.transaction.repository.TransactionRepository;
import com.atlas.bank.atlas.transaction.service.event.TransactionExecutedEvent;
import com.atlas.bank.atlas.transaction.service.factory.TransactionFactory;
import com.atlas.bank.atlas.transaction.service.fee.FeeCalculator;
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

    public TransferService(TransactionRepository transactionRepository,
                           AccountRepository accountRepository,
                           List<FeeCalculator> feeCalculators,
                           ApplicationEventPublisher eventPublisher) {
        super(transactionRepository);
        this.accountRepository = accountRepository;
        this.feeCalculators = feeCalculators;
        this.eventPublisher = eventPublisher;
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
        // Lanzar el evento
        eventPublisher.publishEvent(new TransactionExecutedEvent(
                transaction.getId(),
                transaction.getType(),
                transaction.getSourceAccountId(),
                transaction.getTargetAccountId(),
                transaction.getAmount(),
                transaction.getFee()
        ));
        return transaction;
    }

    @Override
    protected void validate(TransferContext context) {
        // Validar que la cuenta esté activa
        if (!"ACTIVE".equals(context.from().getStatus())) {
            throw new AccountNotActiveException(context.from().getId(), context.from().getStatus());
        }
        if (!"ACTIVE".equals(context.to().getStatus())) {
            throw new AccountNotActiveException(context.to().getId(), context.to().getStatus());
        }

        // Validar fondos
        if (context.from().getBalance().compareTo(context.amount()) < 0) {
            throw new InsufficientFundsException(context.from().getId(), context.from().getBalance(), context.amount());
        }
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
