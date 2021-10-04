package com.ydalal.accounts.db;

import com.google.inject.Inject;
import com.ydalal.accounts.models.Customer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonCustomerDAO extends AbstractDAO implements CustomerDAO{
    private final Map<Integer, Customer> customerMap;

    @Inject
    public JsonCustomerDAO() throws IOException {
        super();
        this.customerMap = new ConcurrentHashMap<>();

        Customer[] customers = mapper.readValue(file, Customer[].class);
        for (Customer customer: customers) {
            customerMap.put(customer.getId(), customer);
        }
    }

    @Override
    public Customer findById(int customerId) {
        return customerMap.get(customerId);
    }

    @Override
    String getPath() {
        return "src/main/resources/customers.json";
    }
}
