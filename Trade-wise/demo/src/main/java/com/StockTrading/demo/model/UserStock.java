// package com.StockTrading.demo.model;

// import org.springframework.stereotype.Component;

// import jakarta.persistence.Entity;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;

// @Component
// @Entity
// @Table
// public class UserStock {
//     @Id
//     private String symbol;
//     private int quantityOwned;
//     private String purchaseDate; 

//     public UserStock(String symbol, int quantityOwned, String purchaseDate) {
//         this.symbol = symbol;
//         this.quantityOwned = quantityOwned;
//         this.purchaseDate = purchaseDate;
//     }

//     // Getters and Setters
//     public String getSymbol() {
//         return symbol;
//     }

//     public void setSymbol(String symbol) {
//         this.symbol = symbol;
//     }

//     public int getQuantityOwned() {
//         return quantityOwned;
//     }

//     public void setQuantityOwned(int quantityOwned) {
//         this.quantityOwned = quantityOwned;
//     }

//     public String getPurchaseDate() {
//         return purchaseDate;
//     }

//     public void setPurchaseDate(String purchaseDate) {
//         this.purchaseDate = purchaseDate;
//     }

//     @Override
//     public String toString() {
//         return "UserStock{" +
//                 "symbol='" + symbol + '\'' +
//                 ", quantityOwned=" + quantityOwned +
//                 ", purchaseDate='" + purchaseDate + '\'' +
//                 '}';
//     }
// }




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

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    // Default constructor
    public UserStock() {
    }

    // Parameterized constructor
    public UserStock(String symbol, int quantityOwned, LocalDate purchaseDate) {
        this.symbol = symbol;
        this.quantityOwned = quantityOwned;
        this.purchaseDate = purchaseDate;
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

    @Override
    public String toString() {
        return "UserStock{" +
                "symbol='" + symbol + '\'' +
                ", quantityOwned=" + quantityOwned +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}
