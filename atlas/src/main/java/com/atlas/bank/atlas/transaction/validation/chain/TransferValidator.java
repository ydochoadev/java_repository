package com.atlas.bank.atlas.transaction.validation.chain;

import com.atlas.bank.atlas.transaction.service.transfer.TransferContext;

public interface TransferValidator {
    void validate(TransferContext context);
}
