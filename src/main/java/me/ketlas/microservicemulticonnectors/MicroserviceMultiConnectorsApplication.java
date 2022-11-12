package me.ketlas.microservicemulticonnectors;

import com.github.javafaker.Faker;
import me.ketlas.microservicemulticonnectors.dtos.AccountRequestDTO;
import me.ketlas.microservicemulticonnectors.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MicroserviceMultiConnectorsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceMultiConnectorsApplication.class, args);
    }

    @Bean
    CommandLineRunner start(AccountService accountService){
        return  args -> {

            Faker faker = new Faker();
            for (int i = 0; i < 50; i++) {

                AccountRequestDTO accountRequestDTO = AccountRequestDTO.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .email(faker.name().username().concat("@gmail.com"))
                        .tel(faker.phoneNumber().cellPhone())
                        .build();
                accountService.saveAccount(accountRequestDTO);
            }

        };
    }
}
