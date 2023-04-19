package com.solutie;

import org.springframework.web.bind.annotation.*;

@RestController
public class PingPongController {

    private static int COUNTER = 0;
    record PingPong(String result){}

    @GetMapping("/ping")
    public PingPong getPingPong(){
        return new PingPong("Pongi %S".formatted(++COUNTER));
    }

}