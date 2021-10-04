package com.ydalal.accounts;

import com.google.inject.AbstractModule;
import com.ydalal.accounts.db.AccountDAO;
import com.ydalal.accounts.db.CustomerDAO;
import com.ydalal.accounts.db.JsonAccountDAO;
import com.ydalal.accounts.db.JsonCustomerDAO;
import com.ydalal.accounts.db.JsonTransactionDAO;
import com.ydalal.accounts.db.TransactionDAO;
import com.ydalal.accounts.validators.Validator;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CustomerDAO.class).to(JsonCustomerDAO.class);
        bind(AccountDAO.class).to(JsonAccountDAO.class);
        bind(TransactionDAO.class).to(JsonTransactionDAO.class);
        bind(Validator.class);
        bind(AccountIdGenerator.class);
    }
}
