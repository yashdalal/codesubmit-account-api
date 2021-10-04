package com.ydalal.accounts.db;

import com.ydalal.accounts.models.Account;

import java.io.IOException;

/**
 * Layer of abstraction between Account DB and Handler. Can be switched out
 * for another implementation if we want to move to real data store in the future
 */
public interface AccountDAO {
    void createAccount(final int customerId,
                       final int initialAmount) throws IOException;

    Account findById(final int accountId);

    void deductFromAccount(final int accountId,
                           final int amount) throws IOException;

    void addToAccount(final int accountId,
                      final int amount) throws IOException;
}
