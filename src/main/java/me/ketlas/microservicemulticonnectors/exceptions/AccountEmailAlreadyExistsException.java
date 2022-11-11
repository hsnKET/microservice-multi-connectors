package me.ketlas.microservicemulticonnectors.exceptions;

public class AccountEmailAlreadyExistsException extends RuntimeException{

    public AccountEmailAlreadyExistsException (String email){
        super(String.format("Account with email '%s' already exists",email));
    }
}
