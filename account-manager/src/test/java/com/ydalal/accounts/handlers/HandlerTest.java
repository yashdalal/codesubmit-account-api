package com.ydalal.accounts.handlers;


import com.google.common.collect.ImmutableList;
import com.ydalal.accounts.db.AccountDAO;
import com.ydalal.accounts.db.TransactionDAO;
import com.ydalal.accounts.exceptions.AccountNotFoundException;
import com.ydalal.accounts.exceptions.CustomerNotFoundException;
import com.ydalal.accounts.exceptions.NotEnoughBalanceException;
import com.ydalal.accounts.models.Account;
import com.ydalal.accounts.models.Transaction;
import com.ydalal.accounts.validators.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HandlerTest {
    private static final int ACCOUNT_ID = 1;
    private static final int SENDER_ACCOUNT_ID = 2;
    private static final int RECEIVER_ACCOUNT_ID = 3;
    private static final int CUSTOMER_ID = 1;
    private static final int INITIAL_AMOUNT = 100;
    private static final int TRANSFER_AMOUNT = 200;
    private static final Account ACCOUNT = Account.builder()
            .id(ACCOUNT_ID)
            .amount(INITIAL_AMOUNT)
            .build();
    private static final List<Transaction> TRANSACTIONS = ImmutableList.of(
            Transaction.builder()
                    .senderId(SENDER_ACCOUNT_ID)
                    .receiverId(RECEIVER_ACCOUNT_ID)
                    .amount(TRANSFER_AMOUNT)
                    .build()
    );

    @Captor
    ArgumentCaptor<Account> accountArgumentCaptor;
    @Mock
    Validator validator;
    @Mock
    AccountDAO accountDAO;
    @Mock
    TransactionDAO transactionDAO;
    @InjectMocks
    Handler handler;

    @Test
    public void createAccount_Success() throws CustomerNotFoundException {
        handler.createAccount(CUSTOMER_ID, INITIAL_AMOUNT);

        verify(validator).validateCustomerExists(CUSTOMER_ID);
        verify(accountDAO).createAccount(accountArgumentCaptor.capture());
        Account account = accountArgumentCaptor.getValue();
        assertThat(account.getAmount()).isEqualTo(INITIAL_AMOUNT);
        assertThat(account.getCustomerId()).isEqualTo(CUSTOMER_ID);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void createAccount_CustomerNotFound() throws CustomerNotFoundException {
        doThrow(new CustomerNotFoundException()).when(validator).validateCustomerExists(CUSTOMER_ID);

        handler.createAccount(CUSTOMER_ID, INITIAL_AMOUNT);
    }

    @Test
    public void transfer_Success() throws AccountNotFoundException, NotEnoughBalanceException {
        handler.transfer(SENDER_ACCOUNT_ID, RECEIVER_ACCOUNT_ID, TRANSFER_AMOUNT);

        verify(validator).validateAccountExists(SENDER_ACCOUNT_ID);
        verify(validator).validateAccountExists(RECEIVER_ACCOUNT_ID);
        verify(validator).validateSufficientAccountBalance(SENDER_ACCOUNT_ID, TRANSFER_AMOUNT);

        verify(accountDAO).deductFromAccount(SENDER_ACCOUNT_ID, TRANSFER_AMOUNT);
        verify(accountDAO).addToAccount(RECEIVER_ACCOUNT_ID, TRANSFER_AMOUNT);

        verify(transactionDAO).recordTransaction(SENDER_ACCOUNT_ID, RECEIVER_ACCOUNT_ID, TRANSFER_AMOUNT);
    }

    @Test(expected = AccountNotFoundException.class)
    public void transfer_SenderAccountDoesNotExist() throws AccountNotFoundException, NotEnoughBalanceException {
        doThrow(new AccountNotFoundException()).when(validator).validateAccountExists(SENDER_ACCOUNT_ID);

        handler.transfer(SENDER_ACCOUNT_ID, RECEIVER_ACCOUNT_ID, TRANSFER_AMOUNT);
    }

    @Test(expected = AccountNotFoundException.class)
    public void transfer_ReceiverAccountDoesNotExist() throws AccountNotFoundException, NotEnoughBalanceException {
        doThrow(new AccountNotFoundException()).when(validator).validateAccountExists(RECEIVER_ACCOUNT_ID);

        handler.transfer(SENDER_ACCOUNT_ID, RECEIVER_ACCOUNT_ID, TRANSFER_AMOUNT);
        verify(validator).validateAccountExists(SENDER_ACCOUNT_ID);
    }

    @Test(expected = NotEnoughBalanceException.class)
    public void transfer_SenderHasInsufficientBalance() throws NotEnoughBalanceException, AccountNotFoundException {
        doThrow(new NotEnoughBalanceException()).when(validator).validateSufficientAccountBalance(SENDER_ACCOUNT_ID, TRANSFER_AMOUNT);

        handler.transfer(SENDER_ACCOUNT_ID, RECEIVER_ACCOUNT_ID, TRANSFER_AMOUNT);
        verify(validator).validateAccountExists(SENDER_ACCOUNT_ID);
        verify(validator).validateAccountExists(RECEIVER_ACCOUNT_ID);
    }

    @Test
    public void retrieveBalance_Success() throws AccountNotFoundException {
        when(accountDAO.findById(ACCOUNT_ID)).thenReturn(ACCOUNT);

        int balance = handler.retrieveBalance(ACCOUNT_ID);

        verify(validator).validateAccountExists(ACCOUNT_ID);
        assertThat(balance).isEqualTo(INITIAL_AMOUNT);
    }

    @Test(expected = AccountNotFoundException.class)
    public void retrieveBalance_AccountDoesNotExist() throws AccountNotFoundException {
        doThrow(new AccountNotFoundException()).when(validator).validateAccountExists(ACCOUNT_ID);

        handler.retrieveBalance(ACCOUNT_ID);
    }

    @Test
    public void retrieveTransferHistory_Success() throws AccountNotFoundException {
        when(transactionDAO.retrieveTransactions(ACCOUNT_ID)).thenReturn(TRANSACTIONS);

        List<Transaction> transactions = handler.retrieveTransferHistory(ACCOUNT_ID);

        assertThat(transactions).isEqualTo(TRANSACTIONS);
        verify(validator).validateAccountExists(ACCOUNT_ID);
    }

    @Test(expected = AccountNotFoundException.class)
    public void retrieveTransferHistory_AccountDoesNotExist() throws AccountNotFoundException {
        doThrow(new AccountNotFoundException()).when(validator).validateAccountExists(ACCOUNT_ID);

        handler.retrieveTransferHistory(ACCOUNT_ID);
    }
}
