package me.ketlas.microservicemulticonnectors.mappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ketlas.microservicemulticonnectors.dtos.AccountRequest;
import me.ketlas.microservicemulticonnectors.dtos.AccountResponse;
import me.ketlas.microservicemulticonnectors.entities.Account;
import org.mapstruct.Mapper;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;



@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponse toAccountResponse(Account account);
    Account toAccount(AccountRequest accountRequest);
}
