package com.test.tdd.rest;

import com.test.tdd.TddApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(classes = TddApplication.class)
@AutoConfigureMockMvc
public class AccountTransactionIntegrationTest {


    @Autowired
    private MockMvc restMockMvc;

    @Test //Test saving money
    public void whenMakeDepositBalanceShouldIncreaseOfAddedAmount() throws Exception {
        fail("Not yet implemented");
    }

    @Test //Test retrieve money
    public void whenMakeWithdrawalBalanceShouldDecreaseOfRemovedAmount(){
        fail("Not yet implemented");
    }

    @Test //Test retrieve money in account without enough cash
    public void whenMakeWithdrawalWithoutEnoughAmountInBalanceSystemExceptionShouldBeRaised(){
        fail("Not yet implemented");
    }

    @Test //Test history
    public void whenPrintingAfterMakingWithdrawalAndDepositHistoryShouldListAllTransactions(){
        fail("Not yet implemented");
    }

}
