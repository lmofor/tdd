package com.test.tdd.rest;

import com.test.tdd.TddApplication;
import com.test.tdd.domain.AccountStatement;
import com.test.tdd.domain.AccountMovement;
import com.test.tdd.domain.enums.EOperation;
import com.test.tdd.exception.BadBalanceException;
import com.test.tdd.repository.AccountMovementRepository;
import com.test.tdd.repository.AccountStatementRepository;
import com.test.tdd.service.AccountMovementService;
import com.test.tdd.service.AccountStatementService;
import com.test.tdd.service.dto.MovementDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TddApplication.class)
@AutoConfigureMockMvc
public class AccountMovementIntegrationTest {

    private AccountStatement ACCOUNT = new AccountStatement(12345L, "12345", new BigDecimal(3000), "Etienne Dupon");;

    @Autowired
    private AccountStatementRepository accountStatementRepository;

    @Autowired
    private AccountMovementRepository accountMovementRepository;

    @Autowired
    private AccountStatementService accountStatementService;

    @Autowired
    private AccountMovementService accountMovementService;

    @Autowired
    private MockMvc restMockMvc;

    @BeforeAll
    public static void beforeAll() {
    }

    @Test //Test saving money
    public void GivenAnAccountWhenMakeDepositBalanceShouldIncreaseOfAddedAmount() throws Exception {
        accountStatementRepository.addAccountStatement(ACCOUNT);
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
    public void GivenAnAccountWhenMakeWithdrawalBalanceShouldDecreaseOfRemovedAmount() throws Exception {
        accountStatementRepository.addAccountStatement(ACCOUNT);
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
    public void GivenAnAccountWhenMakeWithdrawalWithoutEnoughAmountInBalanceSystemExceptionShouldBeRaised() throws Exception {

        accountStatementRepository.addAccountStatement(ACCOUNT);
        BigDecimal balanceBefore = accountStatementService.findByAccountNumber(ACCOUNT.getAccountNumber()).get().getBalance();
        BigDecimal amount = new BigDecimal(5000);
        MovementDTO movement = new MovementDTO(ACCOUNT.getAccountNumber(), amount);
        BigDecimal balanceAfter = balanceBefore;
        int sizeAccountStatementAfter = accountStatementRepository.findAll().size();
        int sizeAccountMovementAfter = accountMovementRepository.findAll().size();

        restMockMvc.perform(post("/api/makewithdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(movement)))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadBalanceException));

        assertThat(sizeAccountMovementAfter).isEqualTo(accountMovementRepository.findAll().size());
        assertThat(sizeAccountStatementAfter).isEqualTo(accountStatementRepository.findAll().size());

        assertThat(balanceAfter).isEqualTo(accountStatementService.findByAccountNumber(ACCOUNT.getAccountNumber()).get().getBalance());


        //fail("Not yet implemented");
    }

    @Test //Test history
    public void GivenAnAccountWhenPrintingAfterMakingWithdrawalAndDepositHistoryShouldListAllTransactions(){
        fail("Not yet implemented");
    }

}
