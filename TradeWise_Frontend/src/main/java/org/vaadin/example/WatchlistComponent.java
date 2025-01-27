package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import java.util.ArrayList;
import java.util.List;

public class WatchlistComponent extends VerticalLayout {
    private Grid<StockItem> grid;
    private List<StockItem> stocks;

    public WatchlistComponent() {
        addClassName("watchlist-component");
        
        H3 title = new H3("Watchlist");
        title.addClassName("watchlist-title");

        createGrid();
        add(title, grid);

        // Add some sample stocks
        addSampleStocks();
    }

    private void createGrid() {
        grid = new Grid<>();
        grid.addClassName("watchlist-grid");

        // Add columns
        grid.addColumn(StockItem::getSymbol).setHeader("Symbol").setFlexGrow(1);
        grid.addColumn(StockItem::getLtp).setHeader("LTP").setFlexGrow(1);
        grid.addColumn(StockItem::getChange).setHeader("Change").setFlexGrow(1);

        // Add component column for actions (visible on hover)
        grid.addComponentColumn(stock -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.addClassName("stock-actions");

            Button buyButton = new Button("Buy");
            buyButton.addClassName("buy-button");
            buyButton.addClickListener(e -> showBuyDialog(stock));

            Button sellButton = new Button("Sell");
            sellButton.addClassName("sell-button");
            sellButton.addClickListener(e -> showSellDialog(stock));

            actions.add(buyButton, sellButton);
            return actions;
        }).setWidth("150px").setFlexGrow(0);

        grid.setAllRowsVisible(true);
    }

    private void addSampleStocks() {
        stocks = new ArrayList<>();
        stocks.add(new StockItem("TCS", 232.75, 1.2));
        stocks.add(new StockItem("INFY", 1432.50, -0.5));
        stocks.add(new StockItem("RELIANCE", 2456.80, 2.1));
        grid.setItems(stocks);
    }

    private void showBuyDialog(StockItem stock) {
        Dialog dialog = new Dialog();
        dialog.addClassName("trade-dialog");

        // Create the buy order form
        TradeOrderForm orderForm = new TradeOrderForm(stock, true);
        dialog.add(orderForm);

        // Add dialog close listener
        dialog.addDialogCloseActionListener(e -> dialog.close());
        
        dialog.open();
    }

    private void showSellDialog(StockItem stock) {
        Dialog dialog = new Dialog();
        dialog.addClassName("trade-dialog");

        // Create the sell order form
        TradeOrderForm orderForm = new TradeOrderForm(stock, false);
        dialog.add(orderForm);

        // Add dialog close listener
        dialog.addDialogCloseActionListener(e -> dialog.close());
        
        dialog.open();
    }

    // Stock item class
    public static class StockItem {
        private String symbol;
        private double ltp;
        private double change;

        public StockItem(String symbol, double ltp, double change) {
            this.symbol = symbol;
            this.ltp = ltp;
            this.change = change;
        }

        public String getSymbol() { return symbol; }
        public double getLtp() { return ltp; }
        public double getChange() { return change; }
    }
}

