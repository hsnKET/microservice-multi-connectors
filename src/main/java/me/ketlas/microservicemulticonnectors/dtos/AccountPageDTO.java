package me.ketlas.microservicemulticonnectors.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountPageDTO {

    private int page;
    private int totalPages;
    private List<AccountResponse> accountResponses;
}
