package com.ydalal.accounts.exceptions;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(final String errorMessage) {
        super(errorMessage);
    }
}
