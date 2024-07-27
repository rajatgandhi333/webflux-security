package com.rsg.webflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rsg")
public class AuthController {


    @GetMapping("/auth")
    public Mono<String> getAuth() {
        return Mono.just("You can not enter the room");
    }


    @GetMapping("/login")
    public Mono<String> loogin() {
        return Mono.just("What brings you her young Padwan?");
    }
}
