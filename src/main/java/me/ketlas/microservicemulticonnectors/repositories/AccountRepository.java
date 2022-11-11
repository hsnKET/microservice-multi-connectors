package me.ketlas.microservicemulticonnectors.repositories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ketlas.microservicemulticonnectors.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;


public interface AccountRepository extends JpaRepository<Account,String> {

    Account findAccountByEmail(String email);

}
