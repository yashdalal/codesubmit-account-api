package com.ydalal.accounts.handlers;

import com.google.inject.Inject;
import com.ydalal.accounts.AccountIdGenerator;
import com.ydalal.accounts.validators.Validator;
import com.ydalal.accounts.db.AccountDAO;
import com.ydalal.accounts.db.TransactionDAO;
import com.ydalal.accounts.exceptions.AccountNotFoundException;
import com.ydalal.accounts.exceptions.CustomerNotFoundException;
import com.ydalal.accounts.exceptions.NotEnoughBalanceException;
import com.ydalal.accounts.models.Account;
import com.ydalal.accounts.models.Transaction;

import java.util.List;

public class Handler {

    private final Validator validator;
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    @Inject
    public Handler(final Validator validator,
                   final AccountDAO accountDAO,
                   final TransactionDAO transactionDAO) {
        this.validator = validator;
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    public void createAccount(final int customerId,
                              final int initialAmount) throws CustomerNotFoundException {
        validator.validateCustomerExists(customerId);

        Account account = Account.builder()
                .id(AccountIdGenerator.generate())
                .customerId(customerId)
                .amount(initialAmount)
                .build();

        accountDAO.createAccount(account);
    }

    public void transfer(final int senderId,
                         final int receiverId,
                         final int transferAmount) throws AccountNotFoundException, NotEnoughBalanceException {
        validator.validateAccountExists(senderId);
        validator.validateAccountExists(receiverId);
        validator.validateSufficientAccountBalance(senderId, transferAmount);

        accountDAO.deductFromAccount(senderId, transferAmount);
        accountDAO.addToAccount(receiverId, transferAmount);

        transactionDAO.recordTransaction(senderId, receiverId, transferAmount);
    }

    public int retrieveBalance(final int accountId) throws AccountNotFoundException {
        validator.validateAccountExists(accountId);

        return accountDAO.findById(accountId).getAmount();
    }

    public List<Transaction> retrieveTransferHistory(final int accountId) throws AccountNotFoundException {
        validator.validateAccountExists(accountId);

        return transactionDAO.retrieveTransactions(accountId);
    }
}
