package com.test.tdd.service.impl;

import com.test.tdd.domain.AccountMovement;
import com.test.tdd.domain.AccountStatement;
import com.test.tdd.domain.enums.EOperation;
import com.test.tdd.exception.BadBalanceException;
import com.test.tdd.repository.AccountMovementRepository;
import com.test.tdd.repository.AccountStatementRepository;
import com.test.tdd.service.AccountMovementService;
import com.test.tdd.service.dto.MovementDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class AccountMovementServiceImpl implements AccountMovementService {

    private final AccountMovementRepository accountMovementRepository;

    private final AccountStatementRepository statementRepository;

    public AccountMovementServiceImpl(AccountMovementRepository accountMovementRepository, AccountStatementRepository statementRepository) {
        this.accountMovementRepository = accountMovementRepository;
        this.statementRepository = statementRepository;
    }

    @Override
    public AccountMovement makeDeposit(MovementDTO movementDTO) {
        Optional<AccountStatement> optional = statementRepository.findByAccountNumber(movementDTO.getAccountNumber());
        if(optional.isPresent()){
            BigDecimal amount = optional.get().getBalance();
                AccountStatement accountStatement = optional.get();
                statementRepository.removeAccountStatement(accountStatement);
                BigDecimal newBalance = amount.add(movementDTO.getAmount());
                accountStatement.setBalance(newBalance);
                statementRepository.addAccountStatement(accountStatement);
                AccountMovement accountMovement = new AccountMovement();
                accountMovement.setAccountNumber(movementDTO.getAccountNumber());
                accountMovement.setAmount(movementDTO.getAmount());
                accountMovement.setOperation(EOperation.DEPOSIT);
                accountMovement.setNewBalance(newBalance);
                accountMovement.setDate(Instant.now());
                accountMovement.setId(accountMovementRepository.findAll().stream().mapToLong(AccountMovement::getId).max().orElse(0)+1);
                accountMovementRepository.addAccountMovement(accountMovement);
                return accountMovement;
        }
        return null;
    }

    @Override
    public AccountMovement makeWithdrawal(MovementDTO movementDTO) {
        Optional<AccountStatement> optional = statementRepository.findByAccountNumber(movementDTO.getAccountNumber());
        if (optional.isPresent()) {
            BigDecimal amount = optional.get().getBalance();
            if (amount.compareTo(movementDTO.getAmount()) < 0)
                throw new BadBalanceException("Votre Solde est insuffisant pour effectuer cette opération !");
            else {
                AccountStatement accountStatement = optional.get();
                statementRepository.removeAccountStatement(accountStatement);
                BigDecimal newBalance = amount.add(movementDTO.getAmount().negate());
                accountStatement.setBalance(newBalance);
                statementRepository.addAccountStatement(accountStatement);
                AccountMovement accountMovement = new AccountMovement();
                accountMovement.setAccountNumber(movementDTO.getAccountNumber());
                accountMovement.setAmount(movementDTO.getAmount());
                accountMovement.setOperation(EOperation.WITHDRAWAL);
                accountMovement.setNewBalance(newBalance);
                accountMovement.setDate(Instant.now());
                accountMovement.setId(accountMovementRepository.findAll().stream().mapToLong(AccountMovement::getId).max().orElse(0)+1);
                accountMovementRepository.addAccountMovement(accountMovement);
                return accountMovement;
            }
        }
        return null;
    }

    @Override
    public List<AccountMovement> showHistory(String accountNumber) {
        return accountMovementRepository.findAllMovementsByAccount(accountNumber);
    }
}
