package com.StockTrading.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

import com.StockTrading.demo.model.StockData;
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
    // @PostMapping("/authenticate")
    // public boolean Auth(@RequestBody Map<String, Object> request) {
    //     String email = (String) request.get("email");
    //     String pass=(String) request.get("password");
    //     boolean res=userService.isValidUser(email,pass);
    //     return res;
    // }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> authenticate(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        // Call the service to authenticate the user
        Map<String, Object> response = userService.authenticateUser(email, password);
        
        if (response != null && response.containsKey("userId")) {
            System.out.println(response);
            return ResponseEntity.ok(response);  // Return successful authentication response
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // Return error response
        }
    }
    
    
    // @PostMapping("/addFunds")
    // public ResponseEntity<String> addFunds(@RequestBody Map<String, Object> request ) {
    //     try {
    //         System.out.println("addfunds");
    //         int userId = (int) request.get("userId");
    //         double amount=(double) request.get("amount");
    //         User updatedUser = userService.addFunds(userId, amount);
    //         return ResponseEntity.ok("Funds added successfully. New balance: " + updatedUser.getAvailableFunds());
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    @PostMapping("/addFunds")
public ResponseEntity<String> addFunds(@RequestBody Map<String, Object> request) {
    try {
        System.out.println("addfunds");

        // Safely extract and convert userId and amount
        Object userIdObj = request.get("userId");
        int userId = 0;
        if (userIdObj instanceof Integer) {
            userId = (Integer) userIdObj;
        } else if (userIdObj instanceof Number) {
            userId = ((Number) userIdObj).intValue();
        } else {
            return ResponseEntity.badRequest().body("Invalid userId format.");
        }

        Object amountObj = request.get("amount");
        double amount = 0.0;
        if (amountObj instanceof Double) {
            amount = (Double) amountObj;
        } else if (amountObj instanceof Number) {
            amount = ((Number) amountObj).doubleValue();
        } else {
            return ResponseEntity.badRequest().body("Invalid amount format.");
        }

        // Call the service to add funds
        User updatedUser = userService.addFunds(userId, amount);

        return ResponseEntity.ok("Funds added successfully. New balance: " + updatedUser.getAvailableFunds());
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    }
    @PostMapping("/withdrawFunds")
public ResponseEntity<String> withDrawFunds(@RequestBody Map<String, Object> request) {
    try {
        System.out.println("withdrawfunds");

        // Safely extract and convert userId and amount
        Object userIdObj = request.get("userId");
        int userId = 0;
        if (userIdObj instanceof Integer) {
            userId = (Integer) userIdObj;
        } else if (userIdObj instanceof Number) {
            userId = ((Number) userIdObj).intValue();
        } else {
            return ResponseEntity.badRequest().body("Invalid userId format.");
        }

        Object amountObj = request.get("amount");
        double amount = 0.0;
        if (amountObj instanceof Double) {
            amount = (Double) amountObj;
        } else if (amountObj instanceof Number) {
            amount = ((Number) amountObj).doubleValue();
        } else {
            return ResponseEntity.badRequest().body("Invalid amount format.");
        }

        // Call the service to add funds
        User updatedUser = userService.withdrawFunds(userId, amount);

        return ResponseEntity.ok("Funds withdrawn successfully. New balance: " + updatedUser.getAvailableFunds());
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    }



    // @PostMapping("/withdrawFunds")
    // public ResponseEntity<String> withdrawFunds(@RequestBody Map<String, Object> request) {
    //     try {
    //         System.out.println("withdrawfunds");
    //         int userId = (int) request.get("userId");
    //         double amount=(double) request.get("amount");
    //         User updatedUser = userService.withdrawFunds(userId, amount);
    //         return ResponseEntity.ok("Funds withdrawn successfully. New balance: " + updatedUser.getAvailableFunds());
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

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

    @PostMapping("/addToWishlist")
    public ResponseEntity<String> addToWishlist(@RequestBody Map<String, Object> request) {
        try {
            // Extract userId
            Integer userId = (Integer) request.get("userId");

            // Extract wishlist stocks
            List<Map<String, Object>> stockList = (List<Map<String, Object>>) request.get("stocks");

            // Add stocks to wishlist for the user
            userService.addStocksToWishlist(userId, stockList);

            return ResponseEntity.ok("Wishlist updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding to wishlist: " + e.getMessage());
        }
    }

     
    @GetMapping("/wishlist/{userId}")
    public List<StockData>getWishlist(@PathVariable Integer userId) {
        List<StockData> wishlist = userService.getWishlistedStocks(userId);
        
        if (!wishlist.isEmpty()) {
            return wishlist;
        } else {
            return wishlist;
        }
    }

    

    


    
}

