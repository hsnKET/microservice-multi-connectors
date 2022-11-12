package me.ketlas.microservicemulticonnectors.web;

import me.ketlas.microservicemulticonnectors.exceptions.AccountEmailAlreadyExistsException;
import me.ketlas.microservicemulticonnectors.exceptions.AccountNotFoundException;
import me.ketlas.microservicemulticonnectors.model.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class AccountExceptionController {


    @ExceptionHandler(value = {AccountNotFoundException.class, AccountEmailAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse>  accountNotFound(RuntimeException ex){
        return new ResponseEntity<ErrorResponse>(
                 ErrorResponse.builder()
                .message(ex.getMessage())
                .build()
                ,new HttpHeaders(),HttpStatus.NOT_FOUND);
    }

}
