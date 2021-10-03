package com.ydalal.accounts.models;

import lombok.Value;

@Value
public class Transaction {
    int senderId;
    int receiverId;
    int amount;
}
