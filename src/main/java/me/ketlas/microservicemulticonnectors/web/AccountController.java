package me.ketlas.microservicemulticonnectors.web;

import me.ketlas.microservicemulticonnectors.dtos.AccountPageDTO;
import me.ketlas.microservicemulticonnectors.dtos.AccountRequestDTO;
import me.ketlas.microservicemulticonnectors.dtos.AccountResponseDTO;
import me.ketlas.microservicemulticonnectors.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public AccountResponseDTO accountDetail(@PathVariable(name = "id") String id){
        return accountService.accountDetails(id);
    }

    @PostMapping("/accounts")
    public AccountResponseDTO saveAccount(@RequestBody AccountRequestDTO accountRequestDTO){
        return accountService.saveAccount(accountRequestDTO);
    }

    @PutMapping("/accounts/{id}")
    public AccountResponseDTO updateAccount(@PathVariable(name =  "id") String id,
                                            @RequestBody AccountRequestDTO accountRequestDTO){
        return accountService.updateAccount(id, accountRequestDTO);
    }

    @DeleteMapping("/accounts/{id}")
    public void updateAccount(@PathVariable(name =  "id") String id){
         accountService.deleteAccount(id);
    }



}
