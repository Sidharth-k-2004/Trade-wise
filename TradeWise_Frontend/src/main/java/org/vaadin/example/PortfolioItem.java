package org.vaadin.example;

public class PortfolioItem {
    private String name;
    private double investmentAmount;
    private String color;
    
    public PortfolioItem(String name, double investmentAmount, String color) {
        this.name = name;
        this.investmentAmount = investmentAmount;
        this.color = color;
    }
    
    public String getName() { return name; }
    public double getInvestmentAmount() { return investmentAmount; }
    public String getColor() { return color; }
}