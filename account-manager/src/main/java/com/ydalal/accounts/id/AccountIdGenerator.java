package com.ydalal.accounts.id;

public class AccountIdGenerator {
    private static int COUNTER = 0;

    public static int generate() {
        return COUNTER++;
    }
}
