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
public class Transaction {
    @JsonProperty("senderId")
    int senderId;
    @JsonProperty("receiverId")
    int receiverId;
    @JsonProperty("transferAmount")
    int transferAmount;
}
