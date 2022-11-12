package me.ketlas.microservicemulticonnectors.services;

import lombok.AllArgsConstructor;
import me.ketlas.microservicemulticonnectors.dtos.AccountPageDTO;
import me.ketlas.microservicemulticonnectors.dtos.AccountRequestDTO;
import me.ketlas.microservicemulticonnectors.dtos.AccountResponseDTO;
import me.ketlas.microservicemulticonnectors.entities.Account;
import me.ketlas.microservicemulticonnectors.exceptions.AccountEmailAlreadyExistsException;
import me.ketlas.microservicemulticonnectors.exceptions.AccountNotFoundException;
import me.ketlas.microservicemulticonnectors.mappers.AccountMapper;
import me.ketlas.microservicemulticonnectors.repositories.AccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private AccountMapper accountMapper;

    @Override
    public AccountPageDTO listAccount(int size, int page) {
        Page<Account> pageAccounts = accountRepository.findAll(PageRequest.of(page,size));
        List<Account> accounts = pageAccounts.getContent();
        AccountPageDTO accountPageDTO = AccountPageDTO.builder()
                .page(page)
                .totalPages(pageAccounts.getTotalPages())
                .accountResponses(accounts.stream()
                        .map(account -> accountMapper.toAccountResponse(account))
                        .collect(Collectors.toList()))
                .build();
        return accountPageDTO;
    }

    @Override
    public AccountResponseDTO saveAccount(AccountRequestDTO accountRequestDTO) {

        Account account = accountRepository.findAccountByEmail(accountRequestDTO.getEmail());
        if (account != null)
            throw new AccountEmailAlreadyExistsException(accountRequestDTO.getEmail());

        Account tempAccount = accountMapper.toAccount(accountRequestDTO);
        tempAccount.setId(UUID.randomUUID().toString());
        tempAccount.setDate(new Date());
        Account savedAccount = accountRepository.save(tempAccount);
        return accountMapper.toAccountResponse(savedAccount);
    }

    @Override
    public AccountResponseDTO updateAccount(String id, AccountRequestDTO accountRequestDTO) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new AccountNotFoundException(id));

        if (accountRequestDTO == null)
            return accountMapper.toAccountResponse(account);

        if (accountRequestDTO.getEmail() != null)
            account.setEmail(accountRequestDTO.getEmail());

        if (accountRequestDTO.getFirstName() != null)
            account.setFirstName(accountRequestDTO.getFirstName());

        if (accountRequestDTO.getLastName() != null)
            account.setLastName(accountRequestDTO.getLastName());

        if (accountRequestDTO.getTel() != null)
            account.setTel(accountRequestDTO.getTel());

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toAccountResponse(savedAccount);
    }

    @Override
    public AccountResponseDTO accountDetails(String id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new AccountNotFoundException(id));
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public void deleteAccount(String id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new AccountNotFoundException(id));
        accountRepository.deleteById(id);

    }

    private boolean isUserAccountExist(String id){
        return accountRepository.findById(id).orElse(null) != null;
    }
}
