package com.test.tdd.service;

import com.test.tdd.domain.AccountMovement;
import com.test.tdd.service.dto.MovementDTO;

import java.util.List;

/**
 * Service Interface for managing {@link AccountMovement}.
 */
public interface AccountMovementService {

    AccountMovement makeDeposit(MovementDTO movementDTO);

    AccountMovement makeWithdrawal(MovementDTO movementDTO);

    List<AccountMovement> showHistory(String accountNumber);


}
