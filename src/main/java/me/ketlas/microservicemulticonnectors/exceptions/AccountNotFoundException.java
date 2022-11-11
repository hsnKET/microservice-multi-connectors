package me.ketlas.microservicemulticonnectors.exceptions;

public class AccountNotFoundException extends RuntimeException{

    public AccountNotFoundException(String accountID){
        super(String.format("Account with ID '%s' not found!",accountID));
    }
}
