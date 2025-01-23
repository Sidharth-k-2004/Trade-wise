package com.StockTrading.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.StockTrading.demo.model.StockData;
import com.StockTrading.demo.service.StockService;
import com.StockTrading.demo.model.UserStock;
import java.util.Map;

@RestController
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/stocks")
    public StockData getStockData(@RequestParam String symbol) {
        StockData stockData = stockService.fetchStockData(symbol);
        if (stockData == null) {
            throw new RuntimeException("Failed to fetch stock data for symbol: " + symbol);
        }
        return stockData;
    }
}

