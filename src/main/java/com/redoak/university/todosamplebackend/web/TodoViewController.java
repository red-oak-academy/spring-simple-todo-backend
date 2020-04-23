package com.redoak.university.todosamplebackend.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TodoViewController {

    @GetMapping
    public String getTodos() {
        return "index";
    }
}
