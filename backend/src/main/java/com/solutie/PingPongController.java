package com.solutie;

@RestController
public class PingPongController {
    record Pingpong(String result){}

    @GetMapping("/ping")
    public PingPong getPingPong(){
        return new Pingpong("Pong");
    }

}