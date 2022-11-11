package me.ketlas.microservicemulticonnectors.services;

import me.ketlas.microservicemulticonnectors.dtos.AccountPageDTO;
import me.ketlas.microservicemulticonnectors.dtos.AccountRequest;
import me.ketlas.microservicemulticonnectors.dtos.AccountResponse;
import me.ketlas.microservicemulticonnectors.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {

    AccountPageDTO listAccount(int size, int page);
    AccountResponse saveAccount(AccountRequest accountRequest);
    AccountResponse updateAccount(String id,AccountRequest accountRequest);
    AccountResponse accountDetails(String id);
    void deleteAccount(String id);

}
