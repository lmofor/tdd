package com.test.tdd.repository;

import com.test.tdd.domain.AccountStatement;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class AccountStatementRepository {

    private final Set<AccountStatement> accountStatements;

    public AccountStatementRepository() {
        accountStatements = new HashSet<>();
        AccountStatement a1 = new AccountStatement(1L,"1",new BigDecimal(2000), "Léon Gomez");
        AccountStatement a3 = new AccountStatement(2L,"2", new BigDecimal(100), "Seynabou Touré");

        AccountStatement a2 = new AccountStatement(3L,"3", new BigDecimal(100), "Milo Serigne");

        AccountStatement a4 = new AccountStatement(4L,"4", new BigDecimal(500),"Alice Belle");

        accountStatements.add(a1);
        accountStatements.add(a2);
        accountStatements.add(a3);
        accountStatements.add(a4);

    }

    public Set<AccountStatement> findAll() {
        return accountStatements;
    }

    public Optional<AccountStatement> findByAccountNumber(String accountNumber) {
        return accountStatements.stream()
                .filter(accountStatement -> accountStatement.getAccountNumber().equals(accountNumber))
                .findFirst();
    }

    public void addAccountStatement(AccountStatement accountStatement) {
        this.accountStatements.add(accountStatement);
    }

    public void removeAccountStatement(AccountStatement accountStatement) {
        this.accountStatements.remove(accountStatement);
    }

}
