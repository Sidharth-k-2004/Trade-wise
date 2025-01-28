package org.vaadin.example;

public class Stock {
    private String symbol;
    private double price;
    private String change;
    private String changePercentage;

    public Stock() {}

    public Stock(String symbol, double price, String change, String changePercentage) {
        this.symbol = symbol;
        this.price = price;
        this.change = change;
        this.changePercentage = changePercentage;
    }

    // Getters and setters
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getChange() { return change; }
    public void setChange(String change) { this.change = change; }
    public String getChangePercentage() { return changePercentage; }
    public void setChangePercentage(String changePercentage) { this.changePercentage = changePercentage; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return symbol.equals(stock.symbol);
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }

    @Override
    public String toString() {
        return "StockData{" +
                "symbol='" + symbol + '\'' +
                ", price='" + price + '\'' +
                ", change='" + change + '\'' +
                ", changePercentage='" + changePercentage + '\'' +
                '}';
    }
}

