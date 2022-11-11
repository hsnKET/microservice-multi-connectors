package me.ketlas.microservicemulticonnectors.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private String firstName;
    private String lastName;
    private String email;
    private String tel;


}
