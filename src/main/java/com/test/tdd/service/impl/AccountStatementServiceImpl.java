package com.test.tdd.service.impl;

import com.test.tdd.domain.AccountStatement;
import com.test.tdd.repository.AccountStatementRepository;
import com.test.tdd.service.AccountStatementService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AccountStatementServiceImpl implements AccountStatementService {

    private final AccountStatementRepository accountStatementRepository ;

    public AccountStatementServiceImpl(AccountStatementRepository accountStatementRepository) {
        this.accountStatementRepository = accountStatementRepository;
    }


    @Override
    public Optional<AccountStatement> findByAccountNumber(String accountNumber) {
        return accountStatementRepository.findByAccountNumber(accountNumber);
    }
}
