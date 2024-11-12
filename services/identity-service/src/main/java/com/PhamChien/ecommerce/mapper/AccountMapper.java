package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.Account;
import com.PhamChien.ecommerce.dto.request.RegisterRequest;
import com.PhamChien.ecommerce.dto.response.AccountResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(RegisterRequest request);
    AccountResponse toAccountResponse(Account account);

}
