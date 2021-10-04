package com.ydalal.accounts.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @JsonProperty("id")
    int id;
    @JsonProperty("customerId")
    int customerId;
    @JsonProperty("balance")
    int balance;
}
