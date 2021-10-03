package com.ydalal.accounts.exceptions;

import lombok.Builder;

@Builder
public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException() {
        super();
    }

    public CustomerNotFoundException(final String errorMessage) {
        super(errorMessage);
    }
}
