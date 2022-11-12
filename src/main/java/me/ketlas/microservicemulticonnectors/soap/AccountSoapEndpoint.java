package me.ketlas.microservicemulticonnectors.soap;

import me.ketlas.microservicemulticonnectors.dtos.AccountPageDTO;
import me.ketlas.microservicemulticonnectors.dtos.AccountRequestDTO;
import me.ketlas.microservicemulticonnectors.dtos.AccountResponseDTO;
import me.ketlas.microservicemulticonnectors.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.stream.Collectors;

@Endpoint
public class AccountSoapEndpoint {

    private final String NAME_SPACE = "http://www.ketlas.me/microservicemulticonnectors/soap";

    @Autowired
    private AccountService accountService;

    @PayloadRoot(namespace = NAME_SPACE,localPart = "accountsRequest")
    @ResponsePayload
    public AccountsResponse getAccounts(@RequestPayload AccountsRequest account){

        AccountsResponse accountsResponse = new AccountsResponse();
        AccountPageDTO accountPageDTO = accountService.listAccount(account.size,account.page);
        accountsResponse.accounts = accountPageDTO.getAccounts().stream()
                .map(accountResponse -> {
                    Account acc = toSoapAccount(accountResponse);
                    return acc;
                }).collect(Collectors.toList());
        accountsResponse.page = accountPageDTO.getPage();
        accountsResponse.totalPage = accountPageDTO.getTotalPages();

        return accountsResponse;
    }

    @PayloadRoot(namespace = NAME_SPACE,localPart = "accountDetailsRequest")
    @ResponsePayload
    public Account accountDetails(@RequestPayload AccountDetailsRequest accountDetailsRequest){
        AccountResponseDTO myAcc = accountService.accountDetails(accountDetailsRequest.id);

        return toSoapAccount(myAcc);
    }

    @PayloadRoot(namespace = NAME_SPACE,localPart = "saveAccountRequest")
    @ResponsePayload
    public SaveAccountResponse saveAccount(@RequestPayload SaveAccountRequest account){
        AccountRequestDTO accountRequest = AccountRequestDTO
                .builder()
                .firstName(account.firstName)
                .lastName(account.lastName)
                .email(account.email)
                .tel(account.tel)
                .build();
        return toSoapSaveAccountResponse(accountService.saveAccount(accountRequest));
    }



    @PayloadRoot(namespace = NAME_SPACE,localPart = "updateAccountRequest")
    @ResponsePayload
    public Account updateAccount(@RequestPayload UpdateAccountRequest updateAccountRequest){
        AccountResponseDTO accountResponseDTO = accountService.updateAccount(updateAccountRequest.id,
                toAccountRequestDTO(updateAccountRequest.account));
        return toSoapAccount(accountResponseDTO);
    }

    @PayloadRoot(namespace = NAME_SPACE,localPart = "deleteAccountRequest")
    @ResponsePayload
    public DeleteAccountResponse deleteAccount(@RequestPayload DeleteAccountRequest deleteAccountRequest){
         accountService.deleteAccount(deleteAccountRequest.id);
         DeleteAccountResponse deleteAccountResponse = new DeleteAccountResponse();
         deleteAccountResponse.setDeleted(true);
         return deleteAccountResponse;
    }


    SaveAccountResponse toSoapSaveAccountResponse(AccountResponseDTO myAcc){
        SaveAccountResponse savedAccountResponse = new SaveAccountResponse();
        savedAccountResponse.setId(myAcc.getId());
        savedAccountResponse.setFirstName(myAcc.getFirstName());
        savedAccountResponse.setLastName(myAcc.getLastName());
        savedAccountResponse.setEmail(myAcc.getEmail());
        savedAccountResponse.setDate(myAcc.getDate());
        savedAccountResponse.setTel(myAcc.getTel());

        return savedAccountResponse;
    }


    Account toSoapAccount(AccountResponseDTO myAcc){
        Account account = new Account();
        account.setId(myAcc.getId());
        account.setFirstName(myAcc.getFirstName());
        account.setLastName(myAcc.getLastName());
        account.setEmail(myAcc.getEmail());
        account.setDate(myAcc.getDate());
        account.setTel(myAcc.getTel());

        return account;
    }


    AccountRequestDTO toAccountRequestDTO(Account account){
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setFirstName(account.getFirstName());
        accountRequestDTO.setLastName(account.getLastName());
        accountRequestDTO.setEmail(account.getEmail());
        accountRequestDTO.setTel(account.getTel());
        return accountRequestDTO;
    }

}
