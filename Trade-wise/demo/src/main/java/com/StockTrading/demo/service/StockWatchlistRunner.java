package com.StockTrading.demo.service;
import com.StockTrading.demo.model.StockData;
import com.StockTrading.demo.model.User;
import com.StockTrading.demo.model.UserStock;
import com.StockTrading.demo.service.StockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StockWatchlistRunner implements CommandLineRunner {
    private final User user;

    private final StockService stockService;

    @Autowired
    public StockWatchlistRunner(StockService stockService, User user) {
        this.stockService = stockService;
        this.user = user;
    }
    

    @Override
    public void run(String... args) throws Exception {
        // Retrieve the list of stock symbols owned by the user
         // String[] watchlist = {"INFY", "TSLA", "MSFT"};
        List<UserStock> ownedStocks = user.getOwnedStocks();
        if (ownedStocks != null) {
            ownedStocks.forEach(userStock -> {
                String symbol = userStock.getSymbol(); // Extract stock symbol
            StockData stockData = stockService.fetchStockData(symbol);
            if (stockData != null) {
                System.out.println(stockData);
            }
            });
        } else {
            System.out.println("No owned stocks found.");
        }
        
    }
}


