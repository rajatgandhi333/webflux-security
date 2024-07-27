package com.rsg.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    //used for authentication and authorization


    //create a password encoder
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //create a dummy  users since we're not creating a database
    //the argument passwordencoder is passed by dependency injection
    @Bean
    public MapReactiveUserDetailsService getMapReactiveUserDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("RCB")
                .password(passwordEncoder.encode("ESCN"))
                .roles("USER")
                .build();


        return new MapReactiveUserDetailsService(user);
    }

    //helps to override default secuirty configs
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {

        return serverHttpSecurity.authorizeExchange(
                        auth -> {
                            auth.pathMatchers("/rsg/login").permitAll();//permitall
                            auth.anyExchange().authenticated();
                        }

                )
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())//disable securitycontextrepository
                .csrf(ServerHttpSecurity.CsrfSpec::disable)//disable csrf
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)//disable formlogin
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)//disable httpbasic
                .build();
    }

}
