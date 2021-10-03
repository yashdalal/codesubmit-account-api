package com.ydalal.accounts.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
    int id;
    int customerId;
    int amount;
}
