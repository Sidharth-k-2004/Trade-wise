// package com.StockTrading.demo.service;

// import com.fasterxml.jackson.databind.JsonNode;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.Optional;

// import com.StockTrading.demo.model.StockData;
// import com.StockTrading.demo.model.User;
// import com.StockTrading.demo.repository.UserRepo;

// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// @Service
// public class StockService {

//     UserRepo userRepo;
//     public StockData fetchStockData(String symbol) {
//         String apiKey = "CLTQ9OB7M0DPLMN7";
//         String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;

//         RestTemplate restTemplate = new RestTemplate();
//         try {
//             Map<String, Map<String, String>> response = restTemplate.getForObject(url, Map.class);
//             Map<String, String> globalQuote = response.get("Global Quote");

//             if (globalQuote == null) {
//                 return null;
//             }

//             StockData stockData = new StockData();
//             stockData.setSymbol(globalQuote.get("01. symbol"));
//             stockData.setPrice(globalQuote.get("05. price"));
//             stockData.setChange(globalQuote.get("09. change"));
//             stockData.setPercentChange(globalQuote.get("10. change percent"));

//             return stockData;
//         } catch (Exception e) {
//             System.err.println("Error fetching stock data: " + e.getMessage());
//             return null;
//         }
//     }
//     public User buyStocks(int userId, String symbol) {
//         Optional<User> optionalUser = userRepo.findById(userId);
//         if (optionalUser.isPresent()) {
//             User user = optionalUser.get();
//             user.toString();
//             StockData stockData = StockService.fetchStockData(symbol);
//             user.addStock(stockData);
//             user.setAvailableFunds(user.getAvailableFunds() - globalQuote.get("05. price"));
//             return userRepo.save(user);
//         } else {
//             throw new IllegalArgumentException("User not found with ID: " + userId);
//         }
//     }
// }



package com.StockTrading.demo.service;

import com.StockTrading.demo.model.StockData;
import com.StockTrading.demo.model.User;
import com.StockTrading.demo.model.UserStock;
import com.StockTrading.demo.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
public class StockService {

    private final UserRepo userRepo;

    // Constructor injection for UserRepo
    public StockService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // Fetch stock data from external API
    public StockData fetchStockData(String symbol) {
        String apiKey = "CLTQ9OB7M0DPLMN7";
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        try {
            Map<String, Map<String, String>> response = restTemplate.getForObject(url, Map.class);
            Map<String, String> globalQuote = response.get("Global Quote");

            if (globalQuote == null) {
                return null; // No data found
            }

            // Map data to StockData object
            StockData stockData = new StockData();
            stockData.setSymbol(globalQuote.get("01. symbol"));
            stockData.setPrice(globalQuote.get("05. price"));
            stockData.setChange(globalQuote.get("09. change"));
            stockData.setPercentChange(globalQuote.get("10. change percent"));

            return stockData;
        } catch (Exception e) {
            System.err.println("Error fetching stock data: " + e.getMessage());
            return null;
        }
    }

    // Buy stocks and update user's portfolio
    public User buyStocks(int userId, String symbol, int quantity) {
        // Fetch user
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        User user = optionalUser.get();

        // Fetch stock data
        StockData stockData = fetchStockData(symbol);
        System.out.println(stockData);
        if (stockData == null) {
            throw new IllegalArgumentException("Stock data not found for symbol: " + symbol);
        }
        double stockPrice;
        try {
            stockPrice = Double.parseDouble(stockData.getPrice());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid stock price received from API");
        }

        double totalCost = stockPrice * quantity;

        if (user.getAvailableFunds() < totalCost) {
            throw new IllegalArgumentException("Insufficient funds to buy " + quantity + " shares of " + symbol);
        }

        user.setAvailableFunds(user.getAvailableFunds() - totalCost);

        LocalDate currentDate = LocalDate.now();
        UserStock stock = new UserStock(symbol, quantity, currentDate);
        user.addStock(stock);

        return userRepo.save(user);
    }
    public User sellStocks(int userId, String symbol, int quantity) {
        // Fetch user
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        User user = optionalUser.get();

        // Fetch stock data
        StockData stockData = fetchStockData(symbol);
        System.out.println(stockData);
        if (stockData == null) {
            throw new IllegalArgumentException("Stock data not found for symbol: " + symbol);
        }
        double stockPrice;
        try {
            stockPrice = Double.parseDouble(stockData.getPrice());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid stock price received from API");
        }

        double totalCost = stockPrice * quantity;

        

        user.setAvailableFunds(user.getAvailableFunds() + totalCost);

        user.removeStock(symbol);

        return userRepo.save(user);
    }
}

