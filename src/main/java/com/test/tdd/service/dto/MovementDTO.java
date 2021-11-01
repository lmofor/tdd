package com.test.tdd.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class MovementDTO implements Serializable {

    private String accountNumber;

    private BigDecimal amount;

    public MovementDTO() {
    }

    public MovementDTO(String accountNumber, BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovementDTO that = (MovementDTO) o;
        return accountNumber.equals(that.accountNumber) && amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, amount);
    }

    @Override
    public String toString() {
        return "MovementDTO{" +
                "accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                '}';
    }
}
