package com.redoak.university.todosamplebackend.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/greetings")
    public String getGreeting() {
        return "Hello, World!";
    }
}
