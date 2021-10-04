package com.ydalal.accounts.db;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.ydalal.accounts.id.AccountIdGenerator;
import com.ydalal.accounts.models.Account;
import com.ydalal.accounts.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonAccountDAO extends AbstractDAO implements AccountDAO {
    private final Map<Integer, Account> accountMap;
    private final List<Account> accounts;

    @Inject
    public JsonAccountDAO() throws IOException {
        super();
        this.accountMap = new ConcurrentHashMap<>();

        if (!FileUtils.fileIsEmpty(file)) {
            accounts = new ArrayList<>(Arrays.asList(mapper.readValue(file, Account[].class)));
            for (Account account: accounts) {
                accountMap.put(account.getId(), account);
            }
        } else {
            this.accounts = new ArrayList<>();
        }
    }

    @Override
    public void createAccount(final int customerId,
                              final int initialAmount) throws IOException {
        Account account = Account.builder()
                .id(AccountIdGenerator.generate())
                .customerId(customerId)
                .balance(initialAmount)
                .build();

        accounts.add(account);
        accountMap.put(account.getId(), account);

        writer.writeValue(file, accounts);
    }

    @Override
    public Account findById(final int accountId) {
        return accountMap.get(accountId);
    }

    @Override
    public void deductFromAccount(final int accountId,
                                  final int transferAmount) throws IOException {
        Account account = findById(accountId);
        int newBalance = account.getBalance() - transferAmount;
        account.setBalance(newBalance);

        updateAccount(account);
    }

    @Override
    public void addToAccount(final int accountId,
                             final int transferAmount) throws IOException {
        Account account = findById(accountId);
        int newBalance = account.getBalance() + transferAmount;
        account.setBalance(newBalance);

        updateAccount(account);
    }

    @VisibleForTesting
    void updateAccount(final Account account) throws IOException {
        for (Account a: accounts) {
            if (account.getId() == a.getId()) {
                a.setBalance(account.getBalance());
            }
        }
        writer.writeValue(file, accounts);
        System.out.println(String.format("Updated account with id %d for customer with id %d. New balance %d",
                account.getId(), account.getCustomerId(), account.getBalance()));

        accountMap.put(account.getId(), account);
    }

    @Override
    String getPath() {
        return "src/main/resources/accounts.json";
    }
}
