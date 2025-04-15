package com.StockTrading.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
    @GetMapping("/")
    public String home() {
        return "Welcome to the application!";
    }
    
}
