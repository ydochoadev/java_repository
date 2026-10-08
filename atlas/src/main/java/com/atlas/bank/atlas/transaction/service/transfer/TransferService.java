package com.atlas.bank.atlas.transaction.service.transfer;

import com.atlas.bank.atlas.account.exeption.AccountNotFoundException;
import com.atlas.bank.atlas.account.model.Account;
import com.atlas.bank.atlas.account.repoditory.DomainAccountRepository;
import com.atlas.bank.atlas.application.port.in.TransferMoneyUseCase;
import com.atlas.bank.atlas.transaction.model.Transaction;
import com.atlas.bank.atlas.transaction.repository.TransactionRepository;
import com.atlas.bank.atlas.transaction.service.domain.TransferDomainService;
import com.atlas.bank.atlas.transaction.service.factory.TransactionFactory;
import com.atlas.bank.atlas.transaction.service.fee.FeeCalculator;
import com.atlas.bank.atlas.transaction.validation.chain.TransferValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService extends TransactionProcessor<TransferContext> implements ITransferService, TransferMoneyUseCase {

    private final DomainAccountRepository accountRepository;
    private final List<FeeCalculator> feeCalculators; // Se tiene TODA las implementaciones
    private final List<TransferValidator> validators;
    private final TransferDomainService transferDomainService;

    public TransferService(TransactionRepository transactionRepository,
                           DomainAccountRepository accountRepository,
                           List<FeeCalculator> feeCalculators,
                           List<TransferValidator> validators,
                           TransferDomainService transferDomainService) {
        super(transactionRepository);
        this.accountRepository = accountRepository;
        this.feeCalculators = feeCalculators;
        this.validators = validators;
        this.transferDomainService = transferDomainService;
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
        // Estados: ciclo de vida de la trx
        transaction.executeTransfer();
        transactionRepository.save(transaction); // Guarda (actualiza) otra vez la trx con su estado. Publica evento

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
        transferDomainService.transfer(context.from(), context.to(), context.amount(), fee);
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
