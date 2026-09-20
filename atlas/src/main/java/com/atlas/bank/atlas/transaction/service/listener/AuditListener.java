package com.atlas.bank.atlas.transaction.service.listener;

import com.atlas.bank.atlas.transaction.service.event.TransactionExecutedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditListener {

    @EventListener
    public void onTransactionExecuted(TransactionExecutedEvent event) {
        log.info("Registrando auditoría - {} de cuenta #{} a cuenta #{} por S/ {}",
                event.type(), event.sourceAccountId(), event.targetAccountId(), event.amount());
    }
}
