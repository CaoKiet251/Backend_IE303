package com.example.Backend_IE303.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
    @GetMapping("/hello")

    public String hello() {
        return  "Hello World";
    }
}
