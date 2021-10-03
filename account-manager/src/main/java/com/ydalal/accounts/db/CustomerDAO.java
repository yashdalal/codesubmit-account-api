package com.ydalal.accounts.db;

import com.ydalal.accounts.models.Customer;

public interface CustomerDAO {
    Customer findById(int customerId);
}
