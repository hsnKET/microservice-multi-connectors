# Microservice Multi Connectors

A simple project demonstrate how to use multi endpoint (SOAP, GraphQL, REST) with Spring

## Project Structure

![image](https://user-images.githubusercontent.com/94486861/201476230-2f172365-9bdc-42d3-abc2-fc9c2cc95143.png)

## SOAP Web Service Structure

![image](https://user-images.githubusercontent.com/94486861/201476277-06fad7a6-94bf-46be-88ca-25ceb7ee0128.png)

## Account Entity
```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    private String id;
    private Long date;
    private String firstName;
    private String lastName;
    private String email;
    private String tel;
    
}
```

## Account Repository
```java
public interface AccountRepository extends JpaRepository<Account,String> {
    Account findAccountByEmail(String email);
}
```

## Account Mapper
```java
@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponseDTO toAccountResponse(Account account);
    Account toAccount(AccountRequestDTO accountRequestDTO);
}
```

<details>
<summary>Implementation</summary>

```java
@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private AccountMapper accountMapper;

    @Override
    public AccountPageDTO listAccount(int size, int page) {
        Page<Account> pageAccounts = accountRepository.findAll(PageRequest.of(page,size));
        List<Account> accounts = pageAccounts.getContent();
        AccountPageDTO accountPageDTO = AccountPageDTO.builder()
                .page(page)
                .totalPages(pageAccounts.getTotalPages())
                .accounts(accounts.stream()
                        .map(account -> accountMapper.toAccountResponse(account))
                        .collect(Collectors.toList()))
                .build();
        return accountPageDTO;
    }

    @Override
    public AccountResponseDTO saveAccount(AccountRequestDTO accountRequestDTO) {

        Account account = accountRepository.findAccountByEmail(accountRequestDTO.getEmail());
        if (account != null)
            throw new AccountEmailAlreadyExistsException(accountRequestDTO.getEmail());

        Account tempAccount = accountMapper.toAccount(accountRequestDTO);
        tempAccount.setId(UUID.randomUUID().toString());
        tempAccount.setDate(new Date().getTime());
        Account savedAccount = accountRepository.save(tempAccount);
        return accountMapper.toAccountResponse(savedAccount);
    }

    @Override
    public AccountResponseDTO updateAccount(String id, AccountRequestDTO accountRequestDTO) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new AccountNotFoundException(id));

        if (accountRequestDTO == null)
            return accountMapper.toAccountResponse(account);

        if (accountRequestDTO.getEmail() != null)
            account.setEmail(accountRequestDTO.getEmail());

        if (accountRequestDTO.getFirstName() != null)
            account.setFirstName(accountRequestDTO.getFirstName());

        if (accountRequestDTO.getLastName() != null)
            account.setLastName(accountRequestDTO.getLastName());

        if (accountRequestDTO.getTel() != null)
            account.setTel(accountRequestDTO.getTel());

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toAccountResponse(savedAccount);
    }

    @Override
    public AccountResponseDTO accountDetails(String id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new AccountNotFoundException(id));
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public void deleteAccount(String id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new AccountNotFoundException(id));
        accountRepository.deleteById(id);

    }

}
```
</details>


## Account Service
```java
public interface AccountService {

    AccountPageDTO listAccount(int size, int page);
    AccountResponseDTO saveAccount(AccountRequestDTO accountRequestDTO);
    AccountResponseDTO updateAccount(String id, AccountRequestDTO accountRequestDTO);
    AccountResponseDTO accountDetails(String id);
    void deleteAccount(String id);

}
```

## Controllers

<details>
<summary>REST</summary>

```java
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
```
</details>

<details>
<summary>GraphQL</summary>

```java
@Controller
@AllArgsConstructor
public class AccountGraphQLController {

    AccountService accountService;

    @QueryMapping
    public AccountPageDTO accounts(@Argument int page,@Argument int size){
        return accountService.listAccount(size,page);
    }

    @QueryMapping
    public AccountResponseDTO accountDetail(@Argument String id){
        return accountService.accountDetails(id);
    }

    @MutationMapping
    public AccountResponseDTO saveAccount(@Argument AccountRequestDTO accountRequestDTO){
        return accountService.saveAccount(accountRequestDTO);
    }

    @MutationMapping
    public AccountResponseDTO updateAccount(@Argument String id,
                                            @Argument AccountRequestDTO accountRequestDTO){
        return accountService.updateAccount(id, accountRequestDTO);
    }

    @MutationMapping
    public void deleteAccount(@Argument String id){
        accountService.deleteAccount(id);
    }

}
```
</details>

## DTOs

<details>
<summary>AccountPageDTO</summary>

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountPageDTO {

    private int page;
    private int totalPages;
    private List<AccountResponseDTO> accounts;
}

```
</details>

<details>
<summary>AccountRequestDTO</summary>

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequestDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String tel;
}

```
</details>

<details>
<summary>AccountResponseDTO</summary>

```java
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
```
</details>

## Exception Handling

<details>
<summary>AccountEmailAlreadyExistsException</summary>

```java
public class AccountEmailAlreadyExistsException extends RuntimeException{

    public AccountEmailAlreadyExistsException (String email){
        super(String.format("Account with email '%s' already exists",email));
    }
}
```
</details>

<details>
<summary>AccountNotFoundException</summary>

```java
public class AccountNotFoundException extends RuntimeException{

    public AccountNotFoundException(String accountID){
        super(String.format("Account with ID '%s' not found!",accountID));
    }
}

```
</details>

<details>
<summary>Global Exception Catcher</summary>

```java
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


```
</details>

## SOAP Web Service

<details>
<summary>AccountSoapEndpoint</summary>

```java
@Endpoint
public class AccountSoapEndpoint {

    private final String NAME_SPACE = "http://www.ketlas.me/microservicemulticonnectors/soap";

    @Autowired
    private AccountService accountService;

    @PayloadRoot(namespace = NAME_SPACE,localPart = "accountsRequest")
    @ResponsePayload
    public AccountsResponse getAccounts(@RequestPayload AccountsRequest account){

        AccountsResponse accountsResponse = new AccountsResponse();
        AccountPageDTO accountPageDTO = accountService.listAccount(account.size,account.page);
        accountsResponse.accounts = accountPageDTO.getAccounts().stream()
                .map(accountResponse -> {
                    Account acc = toSoapAccount(accountResponse);
                    return acc;
                }).collect(Collectors.toList());
        accountsResponse.page = accountPageDTO.getPage();
        accountsResponse.totalPage = accountPageDTO.getTotalPages();

        return accountsResponse;
    }

    @PayloadRoot(namespace = NAME_SPACE,localPart = "accountDetailsRequest")
    @ResponsePayload
    public Account accountDetails(@RequestPayload AccountDetailsRequest accountDetailsRequest){
        AccountResponseDTO myAcc = accountService.accountDetails(accountDetailsRequest.id);

        return toSoapAccount(myAcc);
    }

    @PayloadRoot(namespace = NAME_SPACE,localPart = "saveAccountRequest")
    @ResponsePayload
    public SaveAccountResponse saveAccount(@RequestPayload SaveAccountRequest account){
        AccountRequestDTO accountRequest = AccountRequestDTO
                .builder()
                .firstName(account.firstName)
                .lastName(account.lastName)
                .email(account.email)
                .tel(account.tel)
                .build();
        return toSoapSaveAccountResponse(accountService.saveAccount(accountRequest));
    }



    @PayloadRoot(namespace = NAME_SPACE,localPart = "updateAccountRequest")
    @ResponsePayload
    public Account updateAccount(@RequestPayload UpdateAccountRequest updateAccountRequest){
        AccountResponseDTO accountResponseDTO = accountService.updateAccount(updateAccountRequest.id,
                toAccountRequestDTO(updateAccountRequest.account));
        return toSoapAccount(accountResponseDTO);
    }

    @PayloadRoot(namespace = NAME_SPACE,localPart = "deleteAccountRequest")
    @ResponsePayload
    public DeleteAccountResponse deleteAccount(@RequestPayload DeleteAccountRequest deleteAccountRequest){
        accountService.deleteAccount(deleteAccountRequest.id);
        DeleteAccountResponse deleteAccountResponse = new DeleteAccountResponse();
        deleteAccountResponse.setDeleted(true);
        return deleteAccountResponse;
    }


    SaveAccountResponse toSoapSaveAccountResponse(AccountResponseDTO myAcc){
        SaveAccountResponse savedAccountResponse = new SaveAccountResponse();
        savedAccountResponse.setId(myAcc.getId());
        savedAccountResponse.setFirstName(myAcc.getFirstName());
        savedAccountResponse.setLastName(myAcc.getLastName());
        savedAccountResponse.setEmail(myAcc.getEmail());
        savedAccountResponse.setDate(myAcc.getDate());
        savedAccountResponse.setTel(myAcc.getTel());

        return savedAccountResponse;
    }


    Account toSoapAccount(AccountResponseDTO myAcc){
        Account account = new Account();
        account.setId(myAcc.getId());
        account.setFirstName(myAcc.getFirstName());
        account.setLastName(myAcc.getLastName());
        account.setEmail(myAcc.getEmail());
        account.setDate(myAcc.getDate());
        account.setTel(myAcc.getTel());

        return account;
    }


    AccountRequestDTO toAccountRequestDTO(Account account){
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setFirstName(account.getFirstName());
        accountRequestDTO.setLastName(account.getLastName());
        accountRequestDTO.setEmail(account.getEmail());
        accountRequestDTO.setTel(account.getTel());
        return accountRequestDTO;
    }

}
```
</details>

<details>
<summary>SoapConfig</summary>

```java
@Configuration
@EnableWs
public class SoapConfig {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "account")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("AccountSoap");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://www.ketlas.me/microservicemulticonnectors/soap");
        wsdl11Definition.setSchema(countriesSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema countriesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("account.xsd"));
    }

}
```
</details>

> For more information click [here](https://github.com/hsnKET/microservice-multi-connectors/tree/main/src/main/java/me/ketlas/microservicemulticonnectors/soap)

## Testing

#### OpenAPI (Swagger)

![image](https://user-images.githubusercontent.com/94486861/201477415-4deb89f3-4e81-415f-8f2e-53151c1bb50c.png)


#### SoapUI
![image](https://user-images.githubusercontent.com/94486861/201477445-71c6add3-07a6-435d-ae1e-44a7a791dfe3.png)

#### GraphQL

![image](https://user-images.githubusercontent.com/94486861/201477775-d034ed7a-13e0-4d48-b259-e24fb78ef441.png)


#### Postman

![image](https://user-images.githubusercontent.com/94486861/201477662-a0e5cf5b-b49a-40c7-9c2d-5bef7dd31241.png)

#### Exception

![image](https://user-images.githubusercontent.com/94486861/201477698-83e36508-f107-4aa4-8556-4eae5a97d4c8.png)
