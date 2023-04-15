package com.solutie.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {

}
