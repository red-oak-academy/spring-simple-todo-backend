package com.redoak.university.todosamplebackend.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    private Logger log = LoggerFactory.getLogger(HelloWorldController.class);

    @GetMapping("/greetings")
    public String getGreeting() {
        log.info("/greetings was called.");

        return "Hello, World!";
    }
}
