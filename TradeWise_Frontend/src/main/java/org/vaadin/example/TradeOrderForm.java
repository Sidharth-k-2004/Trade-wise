package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

public class TradeOrderForm extends VerticalLayout {
    
    public TradeOrderForm(WatchlistComponent.StockItem stock, boolean isBuy) {
        addClassName("trade-order-form");

        // Header
        createHeader(stock, isBuy);

        // Trading type selection
        createTradingTypeSelection();

        // Order fields
        createOrderFields();

        // Footer with margin and buttons
        createFooter(stock, isBuy);
    }

    private void createHeader(WatchlistComponent.StockItem stock, boolean isBuy) {
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("trade-dialog-header");
        
        H2 title = new H2((isBuy ? "BUY " : "SELL ") + stock.getSymbol());
        title.addClassName("trade-dialog-title");
        
        header.add(title);
        
        // Exchange prices
        HorizontalLayout exchanges = new HorizontalLayout();
        exchanges.addClassName("exchange-prices");
        
        RadioButtonGroup<String> exchangeGroup = new RadioButtonGroup<>();
        exchangeGroup.setItems("BSE", "NSE");
        exchangeGroup.setValue("BSE");
        
        Span bsePrice = new Span("₹" + stock.getLtp());
        Span nsePrice = new Span("₹" + stock.getLtp());
        
        exchanges.add(exchangeGroup, bsePrice, nsePrice);
        
        add(header, exchanges);
    }

    private void createTradingTypeSelection() {
        RadioButtonGroup<String> tradingType = new RadioButtonGroup<>();
        tradingType.setItems("Intraday", "Longterm");
        tradingType.setValue("Intraday");
        tradingType.addClassName("trading-type-selection");
        
        add(tradingType);
    }

    private void createOrderFields() {
        // Create fields
        NumberField qtyField = new NumberField("Qty");
        qtyField.setMin(1);
        qtyField.setStep(1);
        qtyField.setClassName("order-field");

        NumberField priceField = new NumberField("Price");
        priceField.setClassName("order-field");

        NumberField triggerField = new NumberField("Trigger Price");
        triggerField.setClassName("order-field");

        // Add fields in a horizontal layout
        HorizontalLayout fields = new HorizontalLayout(qtyField, priceField, triggerField);
        fields.addClassName("order-fields");
        
        add(fields);
    }

    private void createFooter(WatchlistComponent.StockItem stock, boolean isBuy) {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addClassName("trade-dialog-footer");
        
        // Margin required
        Div marginRequired = new Div();
        marginRequired.setText("Margin required : ₹" + stock.getLtp());
        marginRequired.addClassName("margin-required");
        
        // Action buttons
        Button actionButton = new Button(isBuy ? "BUY" : "SELL");
        actionButton.addClassName(isBuy ? "buy-action-button" : "sell-action-button");
        
        Button cancelButton = new Button("CANCEL");
        cancelButton.addClassName("cancel-button");
        
        HorizontalLayout buttons = new HorizontalLayout(actionButton, cancelButton);
        buttons.addClassName("action-buttons");
        
        footer.add(marginRequired, buttons);
        add(footer);
    }
}

