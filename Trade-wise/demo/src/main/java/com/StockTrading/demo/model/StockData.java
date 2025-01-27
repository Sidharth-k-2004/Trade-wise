package com.StockTrading.demo.model;


import com.StockTrading.demo.model.UserStock;
public class StockData {
    private String symbol;
    private String price;
    private String change;
    private String percentChange;
    

    // Getters and setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    @Override
    public String toString() {
        return "StockData{" +
                "symbol='" + symbol + '\'' +
                ", price='" + price + '\'' +
                ", change='" + change + '\'' +
                ", percentChange='" + percentChange + '\'' +
                '}';
    }
}
