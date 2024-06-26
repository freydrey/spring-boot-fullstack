package com.solutie.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("List")
public class CustomerListDataAccessService implements CustomerDAO{
    private static List<Customer> customers;

    static {
        customers = new ArrayList<>();

        Customer alex = new Customer(
                1,
                "alex@gmail.com", "password", "Alex",
                21,
                Gender.MALE);
        customers.add(alex);

        Customer jamila = new Customer(
                1,
                "jamila@gmail.com", "password", "Jamila",
                21,
                Gender.MALE);
        customers.add(jamila);

    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean personWithEmailExists(String email) {
        return customers.stream()
                .anyMatch(c->c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(customers :: remove);
    }

    @Override
    public boolean personWithIdExists(Integer id) {
        return customers.stream()
                .anyMatch(c->c.getId().equals(id));
    }

    @Override
    public void updateCustomer(Customer customer) {
       customers.add(customer);
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        return customers.stream()
                .filter(c -> c.getUsername().equals(email))
                .findFirst();
    }
}
