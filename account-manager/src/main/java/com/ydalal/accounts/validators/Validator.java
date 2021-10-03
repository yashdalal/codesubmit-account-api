package com.ydalal.accounts.validators;

import com.ydalal.accounts.db.AccountDAO;
import com.ydalal.accounts.db.CustomerDAO;
import com.ydalal.accounts.exceptions.AccountNotFoundException;
import com.ydalal.accounts.exceptions.CustomerNotFoundException;
import com.ydalal.accounts.exceptions.NotEnoughBalanceException;
import com.ydalal.accounts.models.Account;
import com.ydalal.accounts.models.Customer;

public class Validator {
    private final CustomerDAO customerDAO;
    private final AccountDAO accountDAO;

    public Validator(final CustomerDAO customerDAO,
                     final AccountDAO accountDAO) {
        this.customerDAO = customerDAO;
        this.accountDAO = accountDAO;
    }

    public void validateCustomerExists(final int customerId) throws CustomerNotFoundException {
        Customer customer = customerDAO.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(String.format("Customer %d does not exist", customerId));
        }
    }

    public void validateAccountExists(final int accountId) throws AccountNotFoundException {
        Account account = accountDAO.findById(accountId);

        if (account == null) {
            throw new AccountNotFoundException(String.format("Account %d does not exist", accountId));
        }
    }

    public void validateSufficientAccountBalance(final int accountId,
                                                 final int transferAmount) throws NotEnoughBalanceException {
        Account account = accountDAO.findById(accountId);

        if (account.getAmount() < transferAmount) {
            throw new NotEnoughBalanceException(String.format("Account %d does not have sufficient funds" +
                    "to transfer %d", accountId, transferAmount));
        }
    }
}
