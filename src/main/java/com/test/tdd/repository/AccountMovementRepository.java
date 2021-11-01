package com.test.tdd.repository;

import com.test.tdd.domain.AccountMovement;
import com.test.tdd.domain.enums.EOperation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountMovementRepository {

    private final List<AccountMovement> accountMovementList;

    public AccountMovementRepository() {
        accountMovementList = new ArrayList<>();
        AccountMovement a1 = new AccountMovement(12L,"1", EOperation.DEPOSIT
                ,new BigDecimal(1000),new BigDecimal(3000), Instant.now());
        AccountMovement a3 = new AccountMovement(13L,"1", EOperation.WITHDRAWAL
                ,new BigDecimal(500),new BigDecimal(3500), Instant.now());

        AccountMovement a2 = new AccountMovement(22L,"2", EOperation.DEPOSIT
                ,new BigDecimal(500),new BigDecimal(600), Instant.now().minusSeconds(5000000L));

        AccountMovement a4 = new AccountMovement(23L,"2", EOperation.DEPOSIT
                ,new BigDecimal(500),new BigDecimal(1100), Instant.now().minusSeconds(1000000000L));

        accountMovementList.add(a1);
        accountMovementList.add(a2);
        accountMovementList.add(a3);
        accountMovementList.add(a4);

    }

    public List<AccountMovement> findAll() {
        return accountMovementList;
    }

    public List<AccountMovement> findAllByAccountBetweenPeriod(Instant beginDate, Instant endDate, String accountNumber) {
        return accountMovementList.stream()
                .filter(accountMovement -> accountMovement.getAccountNumber().equals(accountNumber)
                && accountMovement.getDate().isAfter(beginDate) && accountMovement.getDate().isBefore(endDate)
                )
                .collect(Collectors.toList());
    }

    public void addAccountMovement(AccountMovement accountMovement) {
        this.accountMovementList.add(accountMovement);
    }
}
