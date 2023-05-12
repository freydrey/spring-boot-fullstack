package com.solutie.customer;


import com.solutie.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll(); //to clear the created customer on run application
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.save(customer);

        //When
        var actual = underTest.existsCustomerByEmail(email);
        //Then
        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerByEmailFailsWhenEmailDoesNotExist() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        //When
        var actual = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actual).isFalse();

    }

    @Test
    void existsCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.save(customer);

        Integer id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        var actual = underTest.existsCustomerById(id);
        //Then
        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerByIdFailsWhenIdDoesNotExist() {
        //Given
        int id = -1;

        //When
        var actual = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isFalse();

    }
}