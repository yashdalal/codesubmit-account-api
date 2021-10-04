package com.ydalal.accounts.handlers;

import com.google.inject.Inject;
import com.ydalal.accounts.validators.Validator;
import com.ydalal.accounts.db.AccountDAO;
import com.ydalal.accounts.db.TransactionDAO;
import com.ydalal.accounts.exceptions.AccountNotFoundException;
import com.ydalal.accounts.exceptions.CustomerNotFoundException;
import com.ydalal.accounts.exceptions.NotEnoughBalanceException;
import com.ydalal.accounts.models.Transaction;

import java.io.IOException;
import java.util.List;

/**
 * Handler class which contains the public facing APIs. Has dependencies on the Account and
 * Transaction DAOs to fetch data.
 */
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

    /**
     * Validates whether the customer exists, throws CustomerNotFoundException otherwise
     * Creates account for input customerId if validation succeeds.
     *
     * @param customerId
     * @param initialAmount
     * @throws CustomerNotFoundException
     * @throws IOException
     */
    public void createAccount(final int customerId,
                              final int initialAmount) throws CustomerNotFoundException, IOException {
        validator.validateCustomerExists(customerId);

        accountDAO.createAccount(customerId, initialAmount);
    }

    /**
     * Validates whether the accounts exist, throws AccountNotFoundException otherwise
     * Validates if the account has enough balace to transfer, throws NotEnoughBalanceException otherwise
     * If validations succeed, transfers money from one account to the other.
     * Also logs successful transactions.
     * @param senderId
     * @param receiverId
     * @param transferAmount
     * @throws AccountNotFoundException
     * @throws NotEnoughBalanceException
     * @throws IOException
     */
    public void transfer(final int senderId,
                         final int receiverId,
                         final int transferAmount) throws AccountNotFoundException, NotEnoughBalanceException, IOException {
        validator.validateAccountExists(senderId);
        validator.validateAccountExists(receiverId);
        validator.validateSufficientAccountBalance(senderId, transferAmount);

        accountDAO.deductFromAccount(senderId, transferAmount);
        accountDAO.addToAccount(receiverId, transferAmount);

        transactionDAO.recordTransaction(senderId, receiverId, transferAmount);
    }

    /**
     * Validates whether the accounts exist, throws AccountNotFoundException otherwise
     * Retrieves balance for the given account id
     * @param accountId
     * @return
     * @throws AccountNotFoundException
     */
    public int retrieveBalance(final int accountId) throws AccountNotFoundException {
        validator.validateAccountExists(accountId);

        return accountDAO.findById(accountId).getBalance();
    }

    /**
     * Validates whether the accounts exist, throws AccountNotFoundException otherwise
     * Retrieves a list of transactions for the given account otherwise
     * @param accountId
     * @return
     * @throws AccountNotFoundException
     */
    public List<Transaction> retrieveTransferHistory(final int accountId) throws AccountNotFoundException {
        validator.validateAccountExists(accountId);

        return transactionDAO.retrieveTransactions(accountId);
    }
}
