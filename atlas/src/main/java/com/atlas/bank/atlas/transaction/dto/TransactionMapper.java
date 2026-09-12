package com.atlas.bank.atlas.transaction.dto;

import com.atlas.bank.atlas.account.dto.CreateAccountRequest;
import com.atlas.bank.atlas.transaction.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionResponse toResponse(Transaction request);
}
