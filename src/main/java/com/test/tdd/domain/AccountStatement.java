package com.test.tdd.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class AccountStatement implements Serializable {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private String customerName;

    public AccountStatement() {
    }

    public AccountStatement(Long id, String accountNumber, BigDecimal balance, String customerName) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.customerName = customerName;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountStatement that = (AccountStatement) o;
        return accountNumber.equals(that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, balance, customerName);
    }



    @Override
    public String toString() {
        return "AccountStatement{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
