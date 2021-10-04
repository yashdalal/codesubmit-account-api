package com.ydalal.accounts.db;

import com.google.inject.Inject;
import com.ydalal.accounts.models.Transaction;
import com.ydalal.accounts.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonTransactionDAO extends AbstractDAO implements TransactionDAO {
    List<Transaction> transactions;

    @Inject
    public JsonTransactionDAO() throws IOException {
        super();
        if (!FileUtils.fileIsEmpty(file)) {
            transactions = new ArrayList<>(Arrays.asList(mapper.readValue(file, Transaction[].class)));
        } else {
            transactions = new ArrayList<>();
        }
    }

    @Override
    public void recordTransaction(int senderId, int receiverId, int transferAmount) throws IOException {
        Transaction newTransaction = Transaction.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .transferAmount(transferAmount)
                .build();

        transactions.add(newTransaction);

        writer.writeValue(file, transactions);
        System.out.printf("$%d was transferred from account %d to account %d%n",
                newTransaction.getTransferAmount(), newTransaction.getSenderId(), newTransaction.getReceiverId());
    }

    @Override
    public List<Transaction> retrieveTransactions(int accountId) {
        List<Transaction> output = new ArrayList<>();

        for (Transaction transaction: transactions) {
            if (transaction.getSenderId() == accountId || transaction.getReceiverId() == accountId) {
                output.add(transaction);
            }
        }

        return output;
    }

    @Override
    String getPath() {
        return "src/main/resources/transactions.json";
    }
}
