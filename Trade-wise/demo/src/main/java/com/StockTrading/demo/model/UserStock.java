
package com.StockTrading.demo.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Entity
@Table(name = "user_stock") // Explicit table name
public class UserStock {
    @Id
    @Column(name = "symbol", unique = true, nullable = false)
    private String symbol;

    @Column(name = "quantity_owned", nullable = false)
    private int quantityOwned;

    @Column(name = "purchase_price")
    private Double purchasePrice;

    @Column(name = "purchase_date")
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
