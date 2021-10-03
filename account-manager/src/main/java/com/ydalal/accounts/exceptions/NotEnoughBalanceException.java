package com.ydalal.accounts.exceptions;

public class NotEnoughBalanceException extends Exception{
    public NotEnoughBalanceException(final String errorMessage) {
        super(errorMessage);
    }
}
