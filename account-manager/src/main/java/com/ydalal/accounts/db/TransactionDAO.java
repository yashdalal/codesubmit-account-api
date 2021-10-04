package com.ydalal.accounts.db;

import com.ydalal.accounts.models.Transaction;

import java.io.IOException;
import java.util.List;

/**
 * Layer of abstraction between Transaction DB and Handler. Can be switched out
 * for another implementation if we want to move to real data store in the future
 */
public interface TransactionDAO {
    void recordTransaction(final int senderId,
                           final int receiverId,
                           final int transferAmount) throws IOException;

    List<Transaction> retrieveTransactions(final int accountId);
}
