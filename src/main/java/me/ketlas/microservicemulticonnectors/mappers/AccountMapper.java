package me.ketlas.microservicemulticonnectors.mappers;

import me.ketlas.microservicemulticonnectors.dtos.AccountRequestDTO;
import me.ketlas.microservicemulticonnectors.dtos.AccountResponseDTO;
import me.ketlas.microservicemulticonnectors.entities.Account;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponseDTO toAccountResponse(Account account);
    Account toAccount(AccountRequestDTO accountRequestDTO);
}
