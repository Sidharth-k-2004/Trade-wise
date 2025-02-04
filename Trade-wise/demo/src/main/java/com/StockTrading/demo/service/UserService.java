package com.StockTrading.demo.service;

import com.StockTrading.demo.model.StockData;
import com.StockTrading.demo.model.User;
import com.StockTrading.demo.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.StockTrading.demo.model.UserStock;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public List<User> getUsers(){
        return userRepo.findAll();
    }
    
    // public boolean isValidUser(String email, String pass) {
    //     Optional<User> userOptional = userRepo.findByEmail(email);
    
    //     if (userOptional.isPresent()) {
    //         User user = userOptional.get();
    //         return user.getPassword().equals(pass);  // Direct string comparison (Not Secure!)
    //     }
        
    //     return false; // User not found
    // }

    public Map<String, Object> authenticateUser(String email, String password) {
        // Check if user exists in the database
        Optional<User> userOptional = userRepo.findByEmail(email);
        
        Map<String, Object> response = new HashMap<>();
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // Check if the password matches
            if (user.getPassword().equals(password)) { // Secure password comparison with hashing is recommended
                response.put("userId", user.getUserId());  // Store userId
                response.put("status", "success");
                System.out.println("hiiiiiiiiiiiiiii");
                return response;
            }
        }

        // If authentication fails
        response.put("status", "error");
        response.put("message", "Invalid email or password");
        return response;
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

    public void addToWishlist(int userId, StockData stock) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.getWishListedStocks().add(stock);
            userRepo.save(user);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }
    
    public void removeFromWishlist(int userId, String stockSymbol) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.getWishListedStocks().removeIf(stock -> stock.getSymbol().equals(stockSymbol));
            userRepo.save(user);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }
    
    public List<StockData> getWishlist(int userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        return optionalUser.map(User::getWishListedStocks).orElse(Collections.emptyList());
    }

    public void addStocksToWishlist(Integer userId, List<Map<String, Object>> stockList) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
    
            // Convert Map to StockData objects and add to wishlist
            for (Map<String, Object> stockData : stockList) {
                StockData stock = new StockData();
                stock.setSymbol((String) stockData.get("symbol"));
                stock.setPrice(stockData.get("price").toString()); // Keeping price as String
                stock.setChange((String) stockData.get("change"));
                stock.setPercentChange((String) stockData.get("percentChange"));
    
                user.addWishListedStock(stock);
            }
    
            // Save updated user with wishlist
            userRepo.save(user);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }
    // public void addStocksToOwnedStock(Integer userId, List<Map<String, Object>> stockList) {
    //     Optional<User> userOptional = userRepo.findById(userId);
    //     if (userOptional.isPresent()) {
    //         User user = userOptional.get();
    //         Double availableFunds=user.getAvailableFunds();
    //         // Convert Map to StockData objects and add to wishlist
    //         for (Map<String, Object> userStock : stockList) {
    //             UserStock stock = new UserStock();
    //             stock.setSymbol((String) userStock.get("symbol"));
    //             if( userStock.get("quantity")).intValue()*userStock.get("price")).doubleValue()>availableFunds){
    //                 continue;

    //             }
    //             stock.setQuantityOwned(((Number) userStock.get("quantity")).intValue());  // Fix casting
    //             // Fix purchase price type casting
    //             stock.setPurchasePrice(((Number) userStock.get("price")).doubleValue()); 
                
    //             stock.setPurchaseDate(LocalDate.now()); 
    //             user.addStock(stock);
    //         }
    
    //         // Save updated user with wishlist
    //         userRepo.save(user);
    //     } else {
    //         throw new IllegalArgumentException("User not found with ID: " + userId);
    //     }
    // }

    public void addStocksToOwnedStock(Integer userId, List<Map<String, Object>> stockList) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Double availableFunds = user.getAvailableFunds();
    
            // Convert Map to StockData objects and add to owned stocks
            for (Map<String, Object> userStock : stockList) {
                int quantity = ((Number) userStock.get("quantity")).intValue();
                double price = ((Number) userStock.get("price")).doubleValue();
                double totalCost = quantity * price;
    
                // Check if user has sufficient funds
                if (totalCost > availableFunds) {
                    continue; // Skip if not enough funds
                }
    
                UserStock stock = new UserStock();
                stock.setSymbol((String) userStock.get("symbol"));
                stock.setQuantityOwned(quantity);
                stock.setPurchasePrice(price);
                stock.setPurchaseDate(LocalDate.now());
    
                // Deduct funds
                availableFunds -= totalCost;
    
                user.addStock(stock);
            }
    
            // Update user's available funds
            user.setAvailableFunds(availableFunds);
            
            // Save updated user with owned stocks
            userRepo.save(user);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }

    public void sellStock(Integer userId, String symbol, int quantity) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<UserStock> ownedStocks = user.getOwnedStocks();
            UserStock stockToSell = null;
    
            for (UserStock stock : ownedStocks) {
                if (stock.getSymbol().equalsIgnoreCase(symbol) && stock.getQuantityOwned() >= quantity) {
                    stockToSell = stock;
                    break;
                }
            }
    
            if (stockToSell == null) {
                throw new IllegalArgumentException("Stock not found or insufficient quantity to sell.");
            }
    
            // Fetch the current stock price
            StockData stockData = fetchStockData(symbol);
            if (stockData == null) {
                throw new IllegalArgumentException("Unable to fetch current stock price.");
            }
    
            double currentPrice = Double.parseDouble(stockData.getPrice());
            double totalSaleValue = currentPrice * quantity;
    
            // Deduct quantity or remove stock if all shares are sold
            if (stockToSell.getQuantityOwned() == quantity) {
                ownedStocks.remove(stockToSell);
            } else {
                stockToSell.setQuantityOwned(stockToSell.getQuantityOwned() - quantity);
            }
    
            // Add sale value to user's available funds
            user.setAvailableFunds(user.getAvailableFunds() + totalSaleValue);
    
            userRepo.save(user);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }
    
    
    

    public List<StockData> getWishlistedStocks(Integer userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        
        return userOptional.map(User::getWishListedStocks).orElse(List.of()); // Return wishlist or empty list
    }
    public List<UserStock> getownedUserStocks(Integer userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        
        return userOptional.map(User::getOwnedStocks).orElse(List.of()); // Return wishlist or empty list
    }


    
    

    
    
    



}
