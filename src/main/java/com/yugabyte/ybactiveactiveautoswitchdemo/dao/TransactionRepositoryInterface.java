package com.yugabyte.ybactiveactiveautoswitchdemo.dao;


import com.yugabyte.ybactiveactiveautoswitchdemo.model.Customer;

import java.util.List;

public interface TransactionRepositoryInterface {
    void save(Customer customer);
    List<Customer> getCustomers();

}
