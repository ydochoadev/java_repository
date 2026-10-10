package com.atlas.bank.atlas.transaction.dto;

import com.atlas.bank.atlas.domain.model.transaction.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionResponse toResponse(Transaction request);
}
