package me.ketlas.microservicemulticonnectors.web;

import me.ketlas.microservicemulticonnectors.dtos.AccountPageDTO;
import me.ketlas.microservicemulticonnectors.dtos.AccountRequest;
import me.ketlas.microservicemulticonnectors.dtos.AccountResponse;
import me.ketlas.microservicemulticonnectors.exceptions.AccountNotFoundException;
import me.ketlas.microservicemulticonnectors.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
    public AccountPageDTO accounts(@RequestParam(name = "page",defaultValue = "0") int page,
                                   @RequestParam(name = "size",defaultValue = "10") int size
                         ){
        return accountService.listAccount(size,page);
    }

    @GetMapping("/accounts/{id}")
    public AccountResponse accountDetail(@PathVariable(name = "id") String id){
        return accountService.accountDetails(id);
    }

    @PostMapping("/accounts")
    public AccountResponse saveAccount(@RequestBody AccountRequest accountRequest){
        return accountService.saveAccount(accountRequest);
    }

    @PutMapping("/accounts/{id}")
    public AccountResponse updateAccount(@PathVariable(name =  "id") String id,
                                         @RequestBody AccountRequest accountRequest){
        return accountService.updateAccount(id,accountRequest);
    }



}
