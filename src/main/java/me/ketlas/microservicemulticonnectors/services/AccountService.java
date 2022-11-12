package me.ketlas.microservicemulticonnectors.services;

import me.ketlas.microservicemulticonnectors.dtos.AccountPageDTO;
import me.ketlas.microservicemulticonnectors.dtos.AccountRequestDTO;
import me.ketlas.microservicemulticonnectors.dtos.AccountResponseDTO;

public interface AccountService {

    AccountPageDTO listAccount(int size, int page);
    AccountResponseDTO saveAccount(AccountRequestDTO accountRequestDTO);
    AccountResponseDTO updateAccount(String id, AccountRequestDTO accountRequestDTO);
    AccountResponseDTO accountDetails(String id);
    void deleteAccount(String id);

}
