package com.solutie.auth;

import com.solutie.customer.Customer;
import com.solutie.customer.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO
) {

}
