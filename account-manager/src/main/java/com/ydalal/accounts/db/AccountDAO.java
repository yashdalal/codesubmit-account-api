package com.ydalal.accounts.db;

import com.ydalal.accounts.models.Account;

public interface AccountDAO {
    boolean createAccount(final Account account);

    Account findById(final int accountId);

    void deductFromAccount(final int accountId,
                           final int amount);

    void addToAccount(final int accountId,
                      final int amount);
}
