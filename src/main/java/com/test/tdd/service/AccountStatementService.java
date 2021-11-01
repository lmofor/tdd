package com.test.tdd.service;

import com.test.tdd.domain.AccountStatement;

import java.util.Optional;


/**
 * Service Interface for managing {@link AccountStatement}.
 */
public interface AccountStatementService {

    Optional<AccountStatement> findByAccountNumber(String accountNumber);
}
