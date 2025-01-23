package com.StockTrading.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

import com.StockTrading.demo.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.StockTrading.demo.model.UserStock;
import com.StockTrading.demo.service.StockService;
import com.StockTrading.demo.service.UserService;



@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    StockService stockService;


    @RequestMapping("/")
    public String requestMethodName() {
        System.out.println("component");
        return "HI";
    }
    
    @GetMapping("/user")
    public List<User> getUser(){
        return userService.getUsers();
    }

    @PostMapping("/user")
    public void addUser(@RequestBody User user) {
        System.out.println(user);
        userService.addUser(user);
    }
    
    @PutMapping("/addFunds")
    public ResponseEntity<String> addFunds(@RequestBody Map<String, Object> request ) {
        try {
            System.out.println("addfunds");
            int userId = (int) request.get("userId");
            double amount=(double) request.get("amount");
            User updatedUser = userService.addFunds(userId, amount);
            return ResponseEntity.ok("Funds added successfully. New balance: " + updatedUser.getAvailableFunds());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/withdrawFunds")
    public ResponseEntity<String> withdrawFunds(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("withdrawfunds");
            int userId = (int) request.get("userId");
            double amount=(double) request.get("amount");
            User updatedUser = userService.withdrawFunds(userId, amount);
            return ResponseEntity.ok("Funds withdrawn successfully. New balance: " + updatedUser.getAvailableFunds());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/stocks")
    public ResponseEntity<String> buyStocks(@RequestBody  Map<String, Object> request) {
        try {
            System.out.println("Buy stocks");
            int userId = (int) request.get("userId");
            String StockSymbol = (String) request.get("symbol");
            int quantity=(int) request.get("quantity");

            User updatedUser = stockService.buyStocks(userId,StockSymbol,quantity);
            return ResponseEntity.ok("Sticks Bought successfully. New balance: " + updatedUser.getAvailableFunds());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }

    


    
}

