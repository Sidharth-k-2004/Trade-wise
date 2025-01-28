package org.vaadin.example.model;

public class StockData {
    private String symbol;
    private double price;

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return symbol + " - ₹" + String.format("%.2f", price);
    }
}

