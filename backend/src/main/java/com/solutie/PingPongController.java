package com.solutie;

import org.springframework.web.bind.annotation.*;

@RestController
public class PingPongController {
    record PingPong(String result){}

    @GetMapping("/ping")
    public PingPong getPingPong(){
        return new PingPong("Pongi");
    }

}