package com.test.tdd.rest;

import com.test.tdd.domain.AccountMovement;
import com.test.tdd.exception.BadBalanceException;
import com.test.tdd.service.AccountMovementService;
import com.test.tdd.service.AccountStatementService;
import com.test.tdd.service.dto.MovementDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;


/**
 * REST controller for manage accounts.
 * <p>
 *
 */
@Controller
@RequestMapping("/api")
public class AccountMovementResource {

    private final Logger log = LoggerFactory.getLogger(AccountMovementResource.class);

    private final AccountStatementService accountStatementService;

    private final AccountMovementService accountMovementService;

    public AccountMovementResource(AccountStatementService accountStatementService, AccountMovementService accountMovementService) {
        this.accountStatementService = accountStatementService;
        this.accountMovementService = accountMovementService;
    }

    /**
     * {@code POST  /makedeposit} : Make a deposit in my account.
     *
     * @param movementDTO the operation parameters.
     * @return the {@link ResponseEntity} with status {@code 202 (Accepted)}, with status {@code 400 (Bad Request)} if the movement amount is less than zero or account number is null, or with status {@code 500 (Internal Server Error)} for other errors.
     */
    @PostMapping("/makedeposit")
    public ResponseEntity<AccountMovement> makeDeposit(@RequestBody MovementDTO movementDTO) {
        log.debug("REST request to make a deposit with parameters : {}", movementDTO);
        if (movementDTO.getAmount() == null || movementDTO.getAmount().compareTo(BigDecimal.ZERO)<=0 || movementDTO.getAccountNumber()==null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bad Amount or Bad Account number! ");
        }
        return ResponseEntity.accepted().body(accountMovementService.makeDeposit(movementDTO));
    }

    /**
     * {@code POST  /makewithdrawal} : Make a withdrawal from my account.
     *
     * @param movementDTO the operation parameters.
     * @return the {@link ResponseEntity} with status {@code 202 (Accepted)}, with status {@code 400 (Bad Request)} if the movement amount is less than zero or account number is null, with status {@code 401 (Unauthorized)} if bad balance or with status {@code 500 (Internal Server Error)} for other errors.
     * @throws BadBalanceException if the withdrawal fails.
     */
    @PostMapping("/makewithdrawal")
    public ResponseEntity<AccountMovement> makeWithdrawal(@RequestBody MovementDTO movementDTO) throws BadBalanceException {
        log.debug("REST request to make a withdrawal with parameters : {}", movementDTO);
        if (movementDTO.getAmount() == null || movementDTO.getAmount().compareTo(BigDecimal.ZERO)<=0 || movementDTO.getAccountNumber()==null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bad Amount or Bad Account number! ");
        }
        return ResponseEntity.accepted().body(accountMovementService.makeWithdrawal(movementDTO));
    }



}
