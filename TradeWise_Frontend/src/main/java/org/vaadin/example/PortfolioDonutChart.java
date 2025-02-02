package org.vaadin.example;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.List;

public class PortfolioDonutChart extends VerticalLayout {
    
    public PortfolioDonutChart(List<PortfolioItem> items) {
        addClassName("donut-chart-container");
        
        // Calculate total investment
        double totalInvestment = items.stream()
                .mapToDouble(PortfolioItem::getInvestmentAmount)
                .sum();
        
        // Create the donut chart
        Div donutChart = new Div();
        donutChart.addClassName("donut-chart");
        
        // Create the segments
        StringBuilder conicGradient = new StringBuilder("conic-gradient(");
        double currentRotation = 0;
        
        // Create legend container
        Div legend = new Div();
        legend.addClassName("chart-legend");
        
        for (PortfolioItem item : items) {
            double percentage = (item.getInvestmentAmount() / totalInvestment) * 100;
            double degrees = (percentage / 100) * 360;
            
            conicGradient.append(item.getColor())
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
            colorBox.getStyle().set("background-color", item.getColor());
            
            Div label = new Div();
            label.setText(String.format("%s: %.1f%%", item.getName(), percentage));
            label.addClassName("legend-label");
            
            legendItem.add(colorBox, label);
            legend.add(legendItem);
        }
        
        // Remove last comma and close the gradient
        conicGradient.setLength(conicGradient.length() - 1);
        conicGradient.append(")");
        
        // Apply the gradient
        donutChart.getStyle().set("background", conicGradient.toString());
        
        // Create center hole
        Div centerHole = new Div();
        centerHole.addClassName("center-hole");
        donutChart.add(centerHole);
        
        add(donutChart, legend);
        
        // Add CSS styles
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
            "} " +
            ".chart-legend { " +
            "   display: flex; " +
            "   flex-wrap: wrap; " +
            "   gap: 10px; " +
            "   justify-content: center; " +
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
}