package com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.mapper;

import com.atlas.bank.atlas_bank.domain.model.transaction.Transaction;
import com.atlas.bank.atlas_bank.infrastructure.adapter.in.rest.dto.response.TransactionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionResponse toResponse(Transaction transaction);
}
