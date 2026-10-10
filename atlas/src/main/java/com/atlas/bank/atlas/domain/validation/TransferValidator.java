package com.atlas.bank.atlas.domain.validation;

import com.atlas.bank.atlas.domain.model.transaction.TransferContext;

public interface TransferValidator {
    void validate(TransferContext context);
}
