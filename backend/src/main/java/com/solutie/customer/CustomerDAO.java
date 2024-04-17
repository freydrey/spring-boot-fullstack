package com.solutie.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> selectAllCustomers();

    Optional<Customer> selectCustomerById(Integer id);

    void insertCustomer(Customer customer);

    boolean personWithEmailExists(String email);

    void deleteCustomerById(Integer id);

    boolean personWithIdExists(Integer id);

    void updateCustomer(Customer update);

    Optional<Customer> selectUserByEmail(String email);
}
