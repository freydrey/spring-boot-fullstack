package com.solutie.customer;

import com.solutie.customer.exception.DuplicateResourceException;
import com.solutie.customer.exception.RequestValidationException;
import com.solutie.customer.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;
    private final CustomerDTOMapper customerDTOMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO, CustomerDTOMapper customerDTOMapper, PasswordEncoder passwordEncoder) {
        this.customerDAO = customerDAO;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerDTO> getAllCustomers(){
        return customerDAO.selectAllCustomers()
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(Integer id){
        return customerDAO.selectCustomerById(id)
                .map(customerDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with id [%s] NOT found"
                        .formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        //check if email exits
        System.out.println("boolean email exists count = " + customerDAO.personWithEmailExists(customerRegistrationRequest.email()));
        if(customerDAO.personWithEmailExists(customerRegistrationRequest.email())){
            throw new DuplicateResourceException("Email already taken");
        }
        //add
        Customer customer = new Customer(
                customerRegistrationRequest.email(),
                passwordEncoder.encode(customerRegistrationRequest.password()),
                customerRegistrationRequest.name(),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender());
        customerDAO.insertCustomer(customer);
    }

    public void deleteCustomerById(Integer id){
        //check if email exits
        if(!customerDAO.personWithIdExists(id)){
           throw new ResourceNotFoundException("Customer with id [%s] not found " .formatted(id));
        }
        //add
        customerDAO.deleteCustomerById(id);
    }

    public void updateCustomer(Integer customerId, CustomerUpdateRequest customerUpdateRequest){

        Customer customer = customerDAO.selectCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with id [%s] NOT found"
                                .formatted(customerId)));

        boolean changes = false;
        //System.out.println("customer name " + customer.getName() + "-" + customerUpdateRequest.name()
            //    .equals(customer.getName()));

        if(customerUpdateRequest.name() != null && !customerUpdateRequest.name()
                .equals(customer.getName()) ){
            customer.setName(customerUpdateRequest.name());
            changes = true;
        }

        if(customerUpdateRequest.email() != null && !customerUpdateRequest.email()
                .equals(customer.getEmail()) ){
            //check if email exits
            if(customerDAO.personWithEmailExists(customerUpdateRequest.email())){
                throw new DuplicateResourceException("Email already taken");
            }

            customer.setEmail(customerUpdateRequest.email());
            changes = true;
        }

        if(customerUpdateRequest.age() != null && !customerUpdateRequest.age()
                .equals(customer.getAge()) ){
            customer.setAge(customerUpdateRequest.age());
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("no data changes found");
        }

        customerDAO.updateCustomer(customer);

    }
}
