package com.test.tdd.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.tdd.domain.enums.EOperation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class AccountMovement implements Serializable {

    private Long id;
    private String accountNumber;
    private EOperation operation;
    private BigDecimal amount;
    private BigDecimal newBalance;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant date;

    public AccountMovement() {
    }

    public AccountMovement(Long id, String accountNumber, EOperation operation, BigDecimal amount, BigDecimal balance, Instant date) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.operation = operation;
        this.amount = amount;
        this.newBalance = balance;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public EOperation getOperation() {
        return operation;
    }

    public void setOperation(EOperation operation) {
        this.operation = operation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountMovement that = (AccountMovement) o;
        return id.equals(that.id) && accountNumber.equals(that.accountNumber) && operation == that.operation && amount.equals(that.amount) && newBalance.equals(that.newBalance) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, operation, amount, newBalance, date);
    }

    @Override
    public String toString() {
        return "AccountMovement{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", operation=" + operation +
                ", amount=" + amount +
                ", balance=" + newBalance +
                ", date=" + date +
                '}';
    }
}
