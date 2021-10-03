package com.ydalal.accounts.db;

import com.ydalal.accounts.models.Transaction;

import java.util.List;

public interface TransactionDAO {
    void recordTransaction(final int senderId,
                           final int receiverId,
                           final int transferAmount);

    List<Transaction> retrieveTransactions(final int accountId);
}
