package com.rsg.webflux.controller;

import com.rsg.webflux.model.LoginResponse;
import com.rsg.webflux.model.User;
import com.rsg.webflux.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rsg")
public class AuthController {

    private final ReactiveUserDetailsService reactiveUserDetailsService;

    @Autowired
    private JWTService jwtService;

    public AuthController(ReactiveUserDetailsService reactiveUserDetailsService) {
        this.reactiveUserDetailsService = reactiveUserDetailsService;
    }


    @GetMapping("/auth")
    public Mono<ResponseEntity<LoginResponse<String>>> getAuth() {
        return Mono.just(
                ResponseEntity.ok(
                        new LoginResponse("You shall enter young padwan", "")
                )
        );
    }


    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse<String>>> login(@RequestBody User user) {
        Mono<UserDetails> dbUser = reactiveUserDetailsService.findByUsername(user.getEmailId()).defaultIfEmpty(null);
        return dbUser.flatMap(u -> {
            //null check
            if (u != null) {
                if (u.getPassword().equals(user.getPassword())) {
                      return Mono.just(
                            ResponseEntity.ok(
                                    new LoginResponse<>(jwtService.generateToken(u.getUsername()), "Success")
                            )
                    );
                } else {
                    return Mono.just(
                            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                                    new LoginResponse<>("invalid user", "Failed")
                            )
                    );
                }
            } else {
                return Mono.just(
                        ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                new LoginResponse<>("Not found", "not found")
                        )
                );
            }
        });
    }
}