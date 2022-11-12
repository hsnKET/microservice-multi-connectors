package me.ketlas.microservicemulticonnectors.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {

    private String id;
    private Long date;
    private String firstName;
    private String lastName;
    private String email;
    private String tel;


}
