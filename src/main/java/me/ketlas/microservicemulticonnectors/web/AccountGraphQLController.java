package me.ketlas.microservicemulticonnectors.web;

import lombok.AllArgsConstructor;
import me.ketlas.microservicemulticonnectors.dtos.AccountPageDTO;
import me.ketlas.microservicemulticonnectors.dtos.AccountRequestDTO;
import me.ketlas.microservicemulticonnectors.dtos.AccountResponseDTO;
import me.ketlas.microservicemulticonnectors.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class AccountGraphQLController {

    AccountService accountService;

    @QueryMapping
    public AccountPageDTO accounts(@Argument int page,@Argument int size){
        return accountService.listAccount(size,page);
    }

    @QueryMapping
    public AccountResponseDTO accountDetail(@Argument String id){
        return accountService.accountDetails(id);
    }

    @MutationMapping
    public AccountResponseDTO saveAccount(@Argument AccountRequestDTO accountRequestDTO){
        return accountService.saveAccount(accountRequestDTO);
    }

    @MutationMapping
    public AccountResponseDTO updateAccount(@Argument String id,
                                            @Argument AccountRequestDTO accountRequestDTO){
        return accountService.updateAccount(id, accountRequestDTO);
    }

    @MutationMapping
    public void deleteAccount(@Argument String id){
         accountService.deleteAccount(id);
    }



}
