package com.ydalal.accounts.validators;

import com.ydalal.accounts.db.AccountDAO;
import com.ydalal.accounts.db.CustomerDAO;
import com.ydalal.accounts.exceptions.AccountNotFoundException;
import com.ydalal.accounts.exceptions.CustomerNotFoundException;
import com.ydalal.accounts.exceptions.NotEnoughBalanceException;
import com.ydalal.accounts.models.Account;
import com.ydalal.accounts.models.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidatorTest {
    private static final int CUSTOMER_ID = 1;
    private static final Customer NULL_CUSTOMER = null;
    private static final Customer CUSTOMER = Customer.builder().build();
    private static final int ACCOUNT_ID = 10;
    private static final int SUFFICIENT_BALANCE = 120;
    private static final int INSUFFICIENT_BALANCE = 90;
    private static final Account NULL_ACCOUNT = null;
    private static final Account ACCOUNT = Account.builder()
            .id(ACCOUNT_ID)
            .amount(SUFFICIENT_BALANCE)
            .build();
    private static final Account INSUFFICIENT_ACCOUNT = Account.builder()
            .id(ACCOUNT_ID)
            .amount(INSUFFICIENT_BALANCE)
            .build();
    private static final int TRANSFER_AMOUNT = 100;

    @Mock
    CustomerDAO customerDAO;
    @Mock
    AccountDAO accountDAO;
    @InjectMocks
    Validator validator;

    @Test
    public void validateCustomerExists() throws CustomerNotFoundException {
        when(customerDAO.findById(CUSTOMER_ID)).thenReturn(CUSTOMER);

        validator.validateCustomerExists(CUSTOMER_ID);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void validateCustomerExists_CustomerNotFound() throws CustomerNotFoundException {
        when(customerDAO.findById(CUSTOMER_ID)).thenReturn(NULL_CUSTOMER);

        validator.validateCustomerExists(CUSTOMER_ID);
    }

    @Test
    public void validateAccountExists() throws AccountNotFoundException {
        when(accountDAO.findById(ACCOUNT_ID)).thenReturn(ACCOUNT);

        validator.validateAccountExists(ACCOUNT_ID);
    }

    @Test(expected = AccountNotFoundException.class)
    public void validateAccountExists_AccountNotFound() throws AccountNotFoundException {
        when(accountDAO.findById(ACCOUNT_ID)).thenReturn(NULL_ACCOUNT);

        validator.validateAccountExists(ACCOUNT_ID);
    }

    @Test
    public void validateSufficientAccountBalance() throws NotEnoughBalanceException {
        when(accountDAO.findById(ACCOUNT_ID)).thenReturn(ACCOUNT);

        validator.validateSufficientAccountBalance(ACCOUNT_ID, TRANSFER_AMOUNT);
    }

    @Test(expected = NotEnoughBalanceException.class)
    public void validateSufficientAccountBalance_NotEnoughBalance() throws NotEnoughBalanceException {
        when(accountDAO.findById(ACCOUNT_ID)).thenReturn(INSUFFICIENT_ACCOUNT);

        validator.validateSufficientAccountBalance(ACCOUNT_ID, TRANSFER_AMOUNT);
    }
}
