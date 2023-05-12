package com.solutie.customer;

import com.solutie.customer.exception.DuplicateResourceException;
import com.solutie.customer.exception.RequestValidationException;
import com.solutie.customer.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();

        //Then
        verify(customerDAO).selectAllCustomers();
    }

    @Test
    void canGetCustomerById() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "foo",
                "foo@gmail.com",
                23,
                Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //When
        Customer actual = underTest.getCustomerById(id);

        //Then
        assertThat(customer).isEqualTo(actual);
    }

    @Test
    void willThrowWhenGetCustomerByIdReturnsEmptyOptional() {
        //Given
        int id = 10;

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] NOT found"
                        .formatted(id));
    }


    @Test
    void addCustomer() {
        //Given
        String email = "alex@gmail.com";

        when(customerDAO.personWithEmailExists(email)).thenReturn(false);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                "alex",email,29, Gender.MALE
        );

        //When
        underTest.addCustomer(customerRegistrationRequest);

        //Then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).insertCustomer(argumentCaptor.capture());

        Customer capturedCustomer = argumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(customerRegistrationRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerRegistrationRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customerRegistrationRequest.age());
        assertThat(capturedCustomer.getGender()).isEqualTo(customerRegistrationRequest.gender());

    }

    @Test
    void willThrowWhenEmailExistWhileaddingACustomer() {
        //Given
        String email = "alex@gmail.com";

        when(customerDAO.personWithEmailExists(email)).thenReturn(true);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                "alex",email,29, Gender.MALE
        );

        //When
        assertThatThrownBy(() -> underTest.addCustomer(customerRegistrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        //Then
        verify(customerDAO, never()).insertCustomer(any());


    }

    @Test
    void deleteCustomerById() {
        //Given
        int id = 10;

        when(customerDAO.personWithIdExists(id)).thenReturn(true);

        //When
        underTest.deleteCustomerById(id);

        //Then
        verify(customerDAO).deleteCustomerById(id);
    }

    @Test
    void willThrowWhenDeleteCustomerByIdNotFound() {
        //Given
        int id = 10;

        when(customerDAO.personWithIdExists(id)).thenReturn(false);

        //When
        assertThatThrownBy(()->underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("Customer with id [%s] not found " .formatted(id));

        //Then
        verify(customerDAO, never()).deleteCustomerById(id);
    }

    @Test
    void canUpdateAllCustomerProperties() {
        //Given
        int id = 1;

        Customer customer = new Customer(
                id,
                "alex",
                "alex@gmail.com",
                23,
                Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "alexandro",newEmail,29, Gender.MALE
        );

        when(customerDAO.personWithEmailExists(newEmail)).thenReturn(false);

        //When
        underTest.updateCustomer(id, updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getGender()).isEqualTo(updateRequest.gender());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        //Given
        int id = 1;

        Customer customer = new Customer(
                id,
                "alex",
                "alex@gmail.com",
                23,
                Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "alexandro",null,null, null
        );

        //When
        underTest.updateCustomer(id, updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());

    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        //Given
        int id = 1;

        Customer customer = new Customer(
                id,
                "alex",
                "alex@gmail.com",
                23,
                Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null,newEmail,null,null
        );

        when(customerDAO.personWithEmailExists(newEmail)).thenReturn(false);

        //When
        underTest.updateCustomer(id, updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);

    }

    @Test
    void canUpdateOnlyCustomerAge() {
        //Given
        int id = 1;

        Customer customer = new Customer(
                id,
                "alex",
                "alex@gmail.com",
                23,
                Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null,null,22, null
        );

        //When
        underTest.updateCustomer(id, updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());

    }

    @Test
    void willThrowWhenTryingtoUpdateAlreadyTakenCustomerEmail() {
        //Given
        int id = 1;

        Customer customer = new Customer(
                id,
                "alex",
                "alex@gmail.com",
                23,
                Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null,newEmail,null, null
        );

        when(customerDAO.personWithEmailExists(newEmail)).thenReturn(true);

        //When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        //Then
        verify(customerDAO, never()).updateCustomer(any());

    }

    @Test
    void willThrowWhenUpdateHasNoChanges() {
        //Given
        int id = 1;

        Customer customer = new Customer(
                id,
                "alex",
                "alex@gmail.com",
                23,
                Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(),customer.getEmail(),customer.getAge(),customer.getGender()
        );

        //When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                        .hasMessage("no data changes found");

        //Then
        verify(customerDAO,never()).updateCustomer(any());

    }


}