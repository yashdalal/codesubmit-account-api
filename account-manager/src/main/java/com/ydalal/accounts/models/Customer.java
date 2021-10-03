package com.ydalal.accounts.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
    final int id;
    final String name;
}
