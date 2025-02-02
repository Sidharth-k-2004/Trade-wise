package org.vaadin.example.model;



import org.springframework.stereotype.Component;

import java.time.LocalDate;


public class UserStock {
    private String symbol;

    private int quantityOwned;

    private Double purchasePrice;

    private LocalDate purchaseDate;

    // Default constructor
    public UserStock() {
    }

    // Parameterized constructor
    public UserStock(String symbol, int quantityOwned,Double purchase_price, LocalDate purchaseDate) {
        this.symbol = symbol;
        this.quantityOwned = quantityOwned;
        this.purchaseDate = purchaseDate;
        this.purchasePrice=purchase_price;
    }

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuantityOwned() {
        return quantityOwned;
    }

    public void setQuantityOwned(int quantityOwned) {
        this.quantityOwned = quantityOwned;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    @Override
    public String toString() {
        return "UserStock{" +
                "symbol='" + symbol + '\'' +
                ", quantityOwned=" + quantityOwned +
                ", purchasePrice=" + purchasePrice +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}
