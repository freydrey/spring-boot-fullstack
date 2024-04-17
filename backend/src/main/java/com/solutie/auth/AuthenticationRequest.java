package com.solutie.auth;

public record AuthenticationRequest(
        String username,
        String password
) {

}
