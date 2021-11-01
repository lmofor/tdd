package com.test.tdd.rest;

import com.test.tdd.domain.AccountMovement;
import com.test.tdd.exception.BadBalanceException;
import com.test.tdd.service.AccountMovementService;
import com.test.tdd.service.AccountStatementService;
import com.test.tdd.service.dto.MovementDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;
import org.wildfly.common.annotation.NotNull;

import java.math.BigDecimal;
import java.util.List;


/**
 * REST controller for manage accounts.
 * <p>
 *
 */
@Controller
@RequestMapping("/api")
public class AccountMovementResource {

    private final Logger log = LoggerFactory.getLogger(AccountMovementResource.class);


    private final AccountMovementService accountMovementService;

    public AccountMovementResource(AccountMovementService accountMovementService) {
        this.accountMovementService = accountMovementService;
    }

    /**
     * {@code POST  /makedeposit} : Make a deposit in my account.
     *
     * @param movementDTO the operation parameters.
     * @return the {@link ResponseEntity} with status {@code 202 (Accepted)} and movement details, with status {@code 400 (Bad Request)} if the movement amount is less than zero or account number is null, or with status {@code 500 (Internal Server Error)} for other errors.
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
     * @return the {@link ResponseEntity} with status {@code 202 (Accepted)} and movement details, with status {@code 400 (Bad Request)} if the movement amount is less than zero or account number is null, with status {@code 401 (Unauthorized)} if bad balance or with status {@code 500 (Internal Server Error)} for other errors.
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

    /**
     * {@code GET  /accounthistory/:accountnumber} : get the account movement history with pathvariable accountnumber.
     *
     * @param accountnumber the account number to retrieve movement.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body list of movement.
     */
    @GetMapping("/accounthistory/{accountnumber}")
    public ResponseEntity<List<AccountMovement>> getAccountHistory(@PathVariable @NotNull String accountnumber) {
        log.debug("REST request to get account movement history for account {}.", accountnumber);
        return ResponseEntity.ok(accountMovementService.showHistory(accountnumber));
    }

}
