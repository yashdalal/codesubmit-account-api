package com.ydalal.accounts.db;

import com.ydalal.accounts.models.Customer;

/**
 * Layer of abstraction between Customer DB and Handler. Can be switched out
 * for another implementation if we want to move to real data store in the future
 */
public interface CustomerDAO {
    Customer findById(int customerId);
}
