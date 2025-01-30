package com.StockTrading.demo.service;

import com.StockTrading.demo.model.User;
import com.StockTrading.demo.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.StockTrading.demo.model.UserStock;
import java.util.*;


@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public List<User> getUsers(){
        return userRepo.findAll();
    }
    
    public User getUsersById(int userId){
        
        return userRepo.findById(userId).orElse(new User());
    }

    public void addUser(User newUser){
       userRepo.save(newUser);
    }

    public void udpateProducts(User newUser){
        userRepo.save(newUser);
    }

    public void deleteUser(int userId){
        userRepo.deleteById(userId);
    }

    // public User addFunds(int userId, double amount) {
    //     Optional<User> optionalUser = userRepo.findById(userId);
    //     if (optionalUser.isPresent()) {
    //         User user = optionalUser.get();
    //         user.setAvailableFunds(user.getAvailableFunds() + amount);
    //         System.out.println("hii");
    //         return userRepo.save(user);
    //     } else {
    //         throw new IllegalArgumentException("User not found with ID: " + userId);
    //     }
    // }


    public User addFunds(int userId, double amount) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            double newBalance = user.getAvailableFunds() + amount;
            user.setAvailableFunds(newBalance);
            
            // Save the updated user back to the database
            userRepo.save(user);
            
            return user;
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }
    
    public User withdrawFunds(int userId, double amount) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.toString();
            user.setAvailableFunds(user.getAvailableFunds() - amount);
            return userRepo.save(user);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }

    public User buyStocks(int userId, double amount) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.toString();
            user.setAvailableFunds(user.getAvailableFunds() - amount);
            return userRepo.save(user);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }



}
