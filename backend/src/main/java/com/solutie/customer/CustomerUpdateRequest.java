package com.solutie.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {

}
