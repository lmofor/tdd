package com.test.tdd.rest;

import com.test.tdd.TddApplication;
import com.test.tdd.domain.AccountMovement;
import com.test.tdd.domain.AccountStatement;
import com.test.tdd.exception.BadBalanceException;
import com.test.tdd.repository.AccountMovementRepository;
import com.test.tdd.repository.AccountStatementRepository;
import com.test.tdd.service.AccountMovementService;
import com.test.tdd.service.AccountStatementService;
import com.test.tdd.service.dto.MovementDTO;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TddApplication.class)
@AutoConfigureMockMvc
class AccountMovementIntegrationTest {

    private final AccountStatement ACCOUNT = new AccountStatement(12345L, "12345", new BigDecimal(3000), "Etienne Dupon");

    @Autowired
    private final AccountStatementRepository accountStatementRepository = new AccountStatementRepository();

    @Autowired
    private final AccountMovementRepository accountMovementRepository = new AccountMovementRepository();

    @Autowired
    private AccountStatementService accountStatementService;

    @Autowired
    private AccountMovementService accountMovementService;

    @Autowired
    private MockMvc restMockMvc;

    @BeforeEach
    public void beforeEach() {
        accountStatementRepository.addAccountStatement(ACCOUNT);
    }

    @AfterEach
    public void afterEach() {
        accountStatementRepository.removeAccountStatement(ACCOUNT);
    }

    @Test //Test saving money
    void givenAnAccountWhenMakeDepositBalanceShouldIncreaseOfAddedAmount() throws Exception {

        BigDecimal balanceBefore = accountStatementService.findByAccountNumber(ACCOUNT.getAccountNumber()).get().getBalance();
        BigDecimal amount = new BigDecimal(152);
        MovementDTO movement = new MovementDTO(ACCOUNT.getAccountNumber(), amount);
        BigDecimal balanceAfter = balanceBefore.add(amount);
        int sizeAccountStatementAfter = accountStatementRepository.findAll().size();
        int sizeAccountMovementAfter = accountMovementRepository.findAll().size() + 1;

        restMockMvc.perform(post("/api/makedeposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(movement)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.accountNumber").value(ACCOUNT.getAccountNumber()))
                .andExpect(jsonPath("$.operation").value("DEPOSIT"))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.newBalance").value(balanceAfter))
                .andExpect(jsonPath("$.date").isNotEmpty())
                .andExpect(jsonPath("$.id").isNotEmpty());

        assertThat(sizeAccountMovementAfter).isEqualTo(accountMovementRepository.findAll().size());
        assertThat(sizeAccountStatementAfter).isEqualTo(accountStatementRepository.findAll().size());

        //fail("Not yet implemented");
    }

    @Test //Test retrieve money
    void givenAnAccountWhenMakeWithdrawalBalanceShouldDecreaseOfRemovedAmount() throws Exception {
        BigDecimal balanceBefore = accountStatementService.findByAccountNumber(ACCOUNT.getAccountNumber()).get().getBalance();
        BigDecimal amount = new BigDecimal(152);
        MovementDTO movement = new MovementDTO(ACCOUNT.getAccountNumber(), amount);
        BigDecimal balanceAfter = balanceBefore.add(amount.negate());
        int sizeAccountStatementAfter = accountStatementRepository.findAll().size();
        int sizeAccountMovementAfter = accountMovementRepository.findAll().size() + 1;

        restMockMvc.perform(post("/api/makewithdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(movement)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.accountNumber").value(ACCOUNT.getAccountNumber()))
                .andExpect(jsonPath("$.operation").value("WITHDRAWAL"))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.newBalance").value(balanceAfter))
                .andExpect(jsonPath("$.date").isNotEmpty())
                .andExpect(jsonPath("$.id").isNotEmpty());

        assertThat(sizeAccountMovementAfter).isEqualTo(accountMovementRepository.findAll().size());
        assertThat(sizeAccountStatementAfter).isEqualTo(accountStatementRepository.findAll().size());


        //fail("Not yet implemented");
    }

    @Test //Test retrieve money in account without enough cash
    void givenAnAccountWhenMakeWithdrawalWithoutEnoughAmountInBalanceSystemExceptionShouldBeRaised() throws Exception {

        BigDecimal balance = accountStatementService.findByAccountNumber(ACCOUNT.getAccountNumber()).get().getBalance();
        BigDecimal amount = new BigDecimal(5000);
        MovementDTO movement = new MovementDTO(ACCOUNT.getAccountNumber(), amount);
        int sizeAccountStatementAfter = accountStatementRepository.findAll().size();
        int sizeAccountMovementAfter = accountMovementRepository.findAll().size();

        restMockMvc.perform(post("/api/makewithdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(movement)))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadBalanceException));

        assertThat(sizeAccountMovementAfter).isEqualTo(accountMovementRepository.findAll().size());
        assertThat(sizeAccountStatementAfter).isEqualTo(accountStatementRepository.findAll().size());

        assertThat(balance).isEqualTo(accountStatementService.findByAccountNumber(ACCOUNT.getAccountNumber()).get().getBalance());


        //fail("Not yet implemented");
    }

    @Test //Test history
    void givenAnAccountWhenPrintingAfterMakingWithdrawalAndDepositHistoryShouldListAllTransactions() throws Exception {

        long size1 = accountMovementRepository.findAll()
                .stream()
                .filter(accountMovement -> accountMovement.getAccountNumber().equals(ACCOUNT.getAccountNumber()))
                .count();
        // movement 1

        BigDecimal amount1 = new BigDecimal(152);
        MovementDTO movement1 = new MovementDTO(ACCOUNT.getAccountNumber(), amount1);
        accountMovementService.makeDeposit(movement1);

        // movement 2
        BigDecimal amount2 = new BigDecimal(50);
        MovementDTO movement2 = new MovementDTO(ACCOUNT.getAccountNumber(), amount2);
        accountMovementService.makeWithdrawal(movement2);

        JSONParser jsonParser= new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);

        JSONArray registeredAccountMovementsJsonArray = (JSONArray) jsonParser
                .parse(
                        TestUtil
                                .convertObjectToJsonString(
                                        accountMovementRepository.findAllMovementsByAccount(ACCOUNT.getAccountNumber()
                                        )
                                )
                );

        MvcResult result = restMockMvc.perform(get("/api/accounthistory/12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").value(
                        registeredAccountMovementsJsonArray
                        ))
                .andExpect(jsonPath("$", hasSize((int) (size1 + 2)))).andReturn();
        //fail("Not yet implemented");
    }

}
