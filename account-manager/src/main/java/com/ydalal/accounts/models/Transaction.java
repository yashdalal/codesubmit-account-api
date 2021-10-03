package com.ydalal.accounts.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Transaction {
    int senderId;
    int receiverId;
    int amount;
}
