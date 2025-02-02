// package org.vaadin.example;

// import com.vaadin.flow.component.html.Div;
// import com.vaadin.flow.component.notification.Notification;
// import com.vaadin.flow.component.orderedlayout.VerticalLayout;
// import java.util.List;
// import java.util.Map;

// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.client.RestTemplate;
// import org.vaadin.example.model.UserStock;

// import java.util.Arrays;
// import java.util.Collections;
// import java.util.HashMap;

// public class PortfolioDonutChart extends VerticalLayout {
//      private final RestTemplate restTemplate = new RestTemplate();
//     private static final Map<String, String> COLOR_MAP = new HashMap<String, String>() {{
//         put("INFY", "#FFB6C1");
//         put("ONGC", "#ADD8E6");
//         put("TCS", "#FFE4B5");
//         put("KPITTECH", "#98FB98");
//         put("QUICKHEAL", "#DDA0DD");
//         put("WIPRO", "#F4A460");
//         put("M&M", "#90EE90");
//         put("RELIANCE", "#B0C4DE");
//         put("HCL", "#D8BFD8");
//     }};
//     private List<UserStock> fetchOwnedStocks(Integer userId) {
//     String url = "http://localhost:8080/addstock/" + userId;
    
//     try {
//         ResponseEntity<UserStock[]> response = restTemplate.exchange(
//             url,
//             HttpMethod.GET,
//             null,
//             UserStock[].class
//         );

//         return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
//     } catch (Exception e) {
//         Notification.show("Error fetching owned stocks: " + e.getMessage());
//         return Collections.emptyList();
//     }
// }

//     public PortfolioDonutChart(List<Map<String, Object>> stocks) {
//         addClassName("donut-chart-container");
        
//         // Calculate total investment for the portfolio
//         double totalPortfolioValue = stocks.stream()
//             .mapToDouble(stock -> (int) stock.get("quantity") * (double) stock.get("investedAmount"))
//             .sum();
            
//         // Create the donut chart
//         Div donutChart = new Div();
//         donutChart.addClassName("donut-chart");
        
//         // Create the segments
//         StringBuilder conicGradient = new StringBuilder("conic-gradient(");
//         double currentRotation = 0;
        
//         // Create legend container
//         Div legend = new Div();
//         legend.addClassName("chart-legend");
        
//         for (Map<String, Object> stock : stocks) {
//             String symbol = (String) stock.get("symbol");
//             int quantity = (int) stock.get("quantity");
//             double investedAmount = (double) stock.get("investedAmount");
//             double stockValue = quantity * investedAmount;
//             double percentage = (stockValue / totalPortfolioValue) * 100;
//             double degrees = (percentage / 100) * 360;
            
//             String color = COLOR_MAP.getOrDefault(symbol, 
//                 "#" + Integer.toHexString((symbol.hashCode() & 0xFFFFFF) | 0x1000000).substring(1));
            
//             conicGradient.append(color)
//                     .append(" ")
//                     .append(currentRotation)
//                     .append("deg ")
//                     .append(currentRotation + degrees)
//                     .append("deg,");
            
//             currentRotation += degrees;
            
//             // Add legend item
//             Div legendItem = new Div();
//             legendItem.addClassName("legend-item");
            
//             Div colorBox = new Div();
//             colorBox.addClassName("color-box");
//             colorBox.getStyle().set("background-color", color);
            
//             Div label = new Div();
//             label.setText(String.format("%s: %.1f%% (₹%.2f)", 
//                 symbol, 
//                 percentage, 
//                 stockValue));
//             label.addClassName("legend-label");
            
//             legendItem.add(colorBox, label);
//             legend.add(legendItem);
//         }
        
//         // Remove last comma and close the gradient
//         if (conicGradient.charAt(conicGradient.length() - 1) == ',') {
//             conicGradient.setLength(conicGradient.length() - 1);
//         }
//         conicGradient.append(")");
        
//         // Apply the gradient
//         donutChart.getStyle().set("background", conicGradient.toString());
        
//         // Create center hole with total portfolio value
//         Div centerHole = new Div();
//         centerHole.addClassName("center-hole");
        
//         // Add total portfolio value to center
//         Div totalValue = new Div();
//         totalValue.setText(String.format("₹%.2f", totalPortfolioValue));
//         totalValue.addClassName("total-value");
//         centerHole.add(totalValue);
        
//         donutChart.add(centerHole);
        
//         add(donutChart, legend);
        
//         // Add CSS styles
//         getElement().executeJs(
//             "const style = document.createElement('style');" +
//             "style.textContent = `" +
//             ".donut-chart-container { " +
//             "   display: flex; " +
//             "   flex-direction: column; " +
//             "   align-items: center; " +
//             "   gap: 20px; " +
//             "   padding: 20px; " +
//             "} " +
//             ".donut-chart { " +
//             "   position: relative; " +
//             "   width: 200px; " +
//             "   height: 200px; " +
//             "   border-radius: 50%; " +
//             "} " +
//             ".center-hole { " +
//             "   position: absolute; " +
//             "   top: 50%; " +
//             "   left: 50%; " +
//             "   transform: translate(-50%, -50%); " +
//             "   width: 70%; " +
//             "   height: 70%; " +
//             "   background-color: white; " +
//             "   border-radius: 50%; " +
//             "   display: flex; " +
//             "   align-items: center; " +
//             "   justify-content: center; " +
//             "} " +
//             ".total-value { " +
//             "   font-size: 16px; " +
//             "   font-weight: bold; " +
//             "   color: var(--lumo-primary-text-color); " +
//             "} " +
//             ".chart-legend { " +
//             "   display: flex; " +
//             "   flex-wrap: wrap; " +
//             "   gap: 10px; " +
//             "   justify-content: center; " +
//             "   max-width: 300px; " +
//             "} " +
//             ".legend-item { " +
//             "   display: flex; " +
//             "   align-items: center; " +
//             "   gap: 5px; " +
//             "   font-size: 14px; " +
//             "} " +
//             ".color-box { " +
//             "   width: 12px; " +
//             "   height: 12px; " +
//             "   border-radius: 50%; " +
//             "} " +
//             "`;" +
//             "document.head.appendChild(style);"
//         );
//     }
// }


package org.vaadin.example;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.vaadin.example.model.UserStock;

import java.util.*;

public class PortfolioDonutChart extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Map<String, String> COLOR_MAP = new HashMap<>() {{
        put("INFY", "#FFB6C1");
        put("ONGC", "#ADD8E6");
        put("TCS", "#FFE4B5");
        put("KPITTECH", "#98FB98");
        put("QUICKHEAL", "#DDA0DD");
        put("WIPRO", "#F4A460");
        put("M&M", "#90EE90");
        put("RELIANCE", "#B0C4DE");
        put("HCL", "#D8BFD8");
    }};

    public PortfolioDonutChart(Integer userId) {
        addClassName("donut-chart-container");

        // Fetch owned stocks
        List<UserStock> ownedStocks = fetchOwnedStocks(userId);
        if (ownedStocks.isEmpty()) {
            Notification.show("No stocks found in portfolio.");
            return;
        }

        // Calculate total investment for the portfolio
        double totalPortfolioValue = ownedStocks.stream()
                .mapToDouble(stock -> stock.getQuantityOwned() * stock.getPurchasePrice())
                .sum();

        // Create the donut chart
        Div donutChart = new Div();
        donutChart.addClassName("donut-chart");

        // Generate segments
        StringBuilder conicGradient = new StringBuilder("conic-gradient(");
        double currentRotation = 0;

        // Create legend container
        Div legend = new Div();
        legend.addClassName("chart-legend");

        for (UserStock stock : ownedStocks) {
            String symbol = stock.getSymbol();
            int quantity = stock.getQuantityOwned();
            double stockValue = quantity * stock.getPurchasePrice();
            double percentage = (stockValue / totalPortfolioValue) * 100;
            double degrees = (percentage / 100) * 360;

            // Assign color dynamically
            String color = COLOR_MAP.getOrDefault(symbol, generateRandomColor(symbol));

            conicGradient.append(color)
                    .append(" ")
                    .append(currentRotation)
                    .append("deg ")
                    .append(currentRotation + degrees)
                    .append("deg,");

            currentRotation += degrees;

            // Add legend item
            Div legendItem = new Div();
            legendItem.addClassName("legend-item");

            Div colorBox = new Div();
            colorBox.addClassName("color-box");
            colorBox.getStyle().set("background-color", color);

            Div label = new Div();
            label.setText(String.format("%s: %.1f%% (₹%.2f)", symbol, percentage, stockValue));
            label.addClassName("legend-label");

            legendItem.add(colorBox, label);
            legend.add(legendItem);
        }

        // Finalize the conic gradient
        if (conicGradient.charAt(conicGradient.length() - 1) == ',') {
            conicGradient.setLength(conicGradient.length() - 1);
        }
        conicGradient.append(")");

        // Apply gradient to chart
        donutChart.getStyle().set("background", conicGradient.toString());

        // Center hole with total portfolio value
        Div centerHole = new Div();
        centerHole.addClassName("center-hole");

        Div totalValue = new Div();
        totalValue.setText(String.format("₹%.2f", totalPortfolioValue));
        totalValue.addClassName("total-value");
        centerHole.add(totalValue);

        donutChart.add(centerHole);
        add(donutChart, legend);

        
        getElement().executeJs(
            "const style = document.createElement('style');" +
            "style.textContent = `" +
            ".donut-chart-container { " +
            "   display: flex; " +
            "   flex-direction: column; " +
            "   align-items: center; " +
            "   gap: 20px; " +
            "   padding: 20px; " +
            "} " +
            ".donut-chart { " +
            "   position: relative; " +
            "   width: 200px; " +
            "   height: 200px; " +
            "   border-radius: 50%; " +
            "} " +
            ".center-hole { " +
            "   position: absolute; " +
            "   top: 50%; " +
            "   left: 50%; " +
            "   transform: translate(-50%, -50%); " +
            "   width: 70%; " +
            "   height: 70%; " +
            "   background-color: white; " +
            "   border-radius: 50%; " +
            "   display: flex; " +
            "   align-items: center; " +
            "   justify-content: center; " +
            "} " +
            ".total-value { " +
            "   font-size: 16px; " +
            "   font-weight: bold; " +
            "   color: var(--lumo-primary-text-color); " +
            "} " +
            ".chart-legend { " +
            "   display: flex; " +
            "   flex-wrap: wrap; " +
            "   gap: 10px; " +
            "   justify-content: center; " +
            "   max-width: 300px; " +
            "} " +
            ".legend-item { " +
            "   display: flex; " +
            "   align-items: center; " +
            "   gap: 5px; " +
            "   font-size: 14px; " +
            "} " +
            ".color-box { " +
            "   width: 12px; " +
            "   height: 12px; " +
            "   border-radius: 50%; " +
            "} " +
            "`;" +
            "document.head.appendChild(style);"
        );
        
    }

    private List<UserStock> fetchOwnedStocks(Integer userId) {
        String url = "http://localhost:8080/addstock/" + userId;
        try {
            ResponseEntity<UserStock[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    UserStock[].class
            );
            return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
        } catch (Exception e) {
            Notification.show("Error fetching owned stocks: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private String generateRandomColor(String symbol) {
        return "#" + Integer.toHexString((symbol.hashCode() & 0xFFFFFF) | 0x1000000).substring(1);
    }
}

