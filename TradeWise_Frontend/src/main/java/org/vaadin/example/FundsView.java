package org.vaadin.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.vaadin.example.model.StockData;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import io.netty.handler.codec.http.HttpMethod;

import com.vaadin.flow.component.notification.Notification;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import com.vaadin.flow.component.notification.Notification;
import org.json.JSONObject;


@Route("funds")
@PageTitle("Funds | TradeWise")
public class FundsView extends AppLayout {
    private RestTemplate restTemplate = new RestTemplate();

    public FundsView() {
        createHeader();
        createDrawer();
        createMainContent();
    }

    private void createHeader() {
        // Logo
        StreamResource logoResource = new StreamResource("logo.png",
                () -> getClass().getResourceAsStream("/images/logo.png"));
        Image logo = new Image(logoResource, "TradeWise");
        logo.setHeight("80px");
        logo.setWidth("180px");
        logo.addClassName("logo");

        // Navigation tabs
        Tabs tabs = new Tabs(
            createTab("Dashboard", VaadinIcon.DASHBOARD, "dashboard"),
            createTab("Orders", VaadinIcon.CART, "orders"),
            createTab("Holdings", VaadinIcon.CHART, "holdings"),
            createTab("Positions", VaadinIcon.TRENDING_UP, "positions"),
            createTab("Funds", VaadinIcon.WALLET, "funds"),
            createTab("Wishlist", VaadinIcon.HEART, "wishlist")
        );
        tabs.addClassName("navigation-tabs");
        
        // Set Funds tab as selected
        tabs.setSelectedIndex(4);

        // Profile button
        Button profileButton = new Button(new Icon(VaadinIcon.USER));
        profileButton.addClassName("profile-button");
    
        // Create the profile menu
        ProfileMenu profileMenu = new ProfileMenu();
    
        // Add click listener to show the menu
        profileButton.addClickListener(event -> {
            if (profileMenu.isOpened()) {
                profileMenu.close();
            } else {
                profileMenu.setOpened(true);
            }
        });

        // Header layout
        HorizontalLayout header = new HorizontalLayout(
            new DrawerToggle(),
            logo,
            tabs,
            profileButton
        );
        header.addClassName("header");
        header.setWidthFull();
        header.setHeight("120px");
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    

    private void createDrawer() {
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search your stocks");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.addClassName("search-field");
    
        VerticalLayout watchlist = new VerticalLayout();
        watchlist.addClassName("watchlist");
        watchlist.add(new H3("Watchlist"));
    
        watchlist.add(createWatchlistTable());
    
        VerticalLayout drawer = new VerticalLayout();
        drawer.addClassName("drawer");
    
        Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
        if (userId != null) {
            try {
                PortfolioDonutChart donutChart = new PortfolioDonutChart(userId); // Pass userId directly
                drawer.add(searchField, watchlist, donutChart);
            } catch (Exception e) {
                Notification.show("Error loading portfolio data: " + e.getMessage());
                drawer.add(searchField, watchlist);
            }
        } else {
            Notification.show("User not logged in");
            drawer.add(searchField, watchlist);
        }
    
        addToDrawer(drawer);
    }

    private Grid<Stock> createWatchlistTable() {
        Grid<Stock> grid = new Grid<>(Stock.class, false);
        grid.setAllRowsVisible(true);

        grid.addColumn(new ComponentRenderer<>(stock -> {
            HorizontalLayout layout = new HorizontalLayout();
            layout.setAlignItems(FlexComponent.Alignment.CENTER);
            layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
            layout.setWidthFull();

            Span symbol = new Span(stock.getSymbol());
            Span price = new Span(String.format("%.2f", stock.getPrice()));
            Span change = new Span(String.format("%.2f", stock.getChange()));

            change.getElement().getThemeList().add(stock.getChange() >= 0 ? "badge success" : "badge error");

            HorizontalLayout infoLayout = new HorizontalLayout(symbol, price, change);
            infoLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            Button buyButton = new Button("Buy", new Icon(VaadinIcon.PLUS));
            buyButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
            buyButton.getStyle().set("display", "none");
            buyButton.addClickListener(e -> showTradeDialog(stock, true));

            Button sellButton = new Button("Sell", new Icon(VaadinIcon.MINUS));
            sellButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            sellButton.getStyle().set("display", "none");
            sellButton.addClickListener(e -> showTradeDialog(stock, true));

            HorizontalLayout buttonLayout = new HorizontalLayout(buyButton, sellButton);
            buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            layout.add(infoLayout, buttonLayout);

            layout.getElement().addEventListener("mouseover", e -> {
                buyButton.getStyle().set("display", "inline-flex");
                sellButton.getStyle().set("display", "inline-flex");
            });

            layout.getElement().addEventListener("mouseout", e -> {
                buyButton.getStyle().set("display", "none");
                sellButton.getStyle().set("display", "none");
            });

            return layout;
        })).setAutoWidth(true);

        grid.setItems(getWishlistStocks());
        grid.addClassName("watchlist-table");

        return grid;
    }
//     public List<Stock> getWishlistStocks() {
//     Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");

//     if (userId == null || userId == -1) {
//         Notification.show("User is not logged in.");
//         return Collections.emptyList();  // Return an empty list instead of null
//     }

//     String url = "http://localhost:8080/wishlist/" + userId;

    
//         List<StockData> stocks = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Stock>>() {}
//         return stocks;

// }
public List<Stock> getWishlistStocks() {
    Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");

    if (userId == null || userId == -1) {
        Notification.show("User is not logged in.");
        return Collections.emptyList();
    }

    String url = "http://localhost:8080/wishlist/" + userId;

    // Fetch the data and map it to an array
    Stock[] stockArray = restTemplate.getForObject(url, Stock[].class);

    // Convert array to list and return
    return stockArray != null ? Arrays.asList(stockArray) : Collections.emptyList();
}



    // private void showTradeDialog(Stock stock, boolean isBuy) {
    //     Dialog dialog = new Dialog();
    //     dialog.setWidth("700px");
        
    //     // Header
    //     H2 title = new H2((isBuy ? "BUY " : "SELL ") + stock.getSymbol());
    //     title.getStyle().set("margin", "0");
    //     title.getStyle().set("font-size", "24px");
    //     title.getStyle().set("color", isBuy ? "var(--lumo-success-color)" : "var(--lumo-error-color)");
        
    //     // Exchange selection
    //     RadioButtonGroup<String> exchange = new RadioButtonGroup<>();
    //     exchange.setItems("BSE", "NSE");
    //     exchange.setValue("BSE");
    //     exchange.setThemeName("horizontal");
        
    //     // Price labels
    //     HorizontalLayout priceLayout = new HorizontalLayout();
    //     Span bsePrice = new Span("₹ " + String.format("%.2f", stock.getPrice()));
    //     Span nsePrice = new Span("₹ " + String.format("%.2f", stock.getPrice()));
    //     priceLayout.add(bsePrice, new Span(" | "), nsePrice);
    //     priceLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        
    //     // Trade type
    //     RadioButtonGroup<String> tradeType = new RadioButtonGroup<>();
    //     tradeType.setItems("Intraday", "Longterm");
    //     tradeType.setValue("Intraday");
    //     tradeType.setThemeName("horizontal");
        
    //     // Input fields
    //     TextField quantityField = new TextField("Qty");
    //     quantityField.setValueChangeMode(ValueChangeMode.EAGER);
        
    //     TextField priceField = new TextField("Price");
    //     priceField.setValueChangeMode(ValueChangeMode.EAGER);
    //     priceField.setValue(String.format("%.2f", stock.getPrice()));
        
    //     TextField triggerField = new TextField("Trigger Price");
    //     triggerField.setValueChangeMode(ValueChangeMode.EAGER);
        
    //     // Fields layout
    //     HorizontalLayout fieldsLayout = new HorizontalLayout(quantityField, priceField, triggerField);
    //     fieldsLayout.setWidthFull();
        
    //     // Margin required section
    //     HorizontalLayout marginLayout = new HorizontalLayout();
    //     Span marginLabel = new Span("Margin required : ");
    //     Span marginAmount = new Span("₹ " + String.format("%.2f", stock.getPrice()));
    //     marginLayout.add(marginLabel, marginAmount);
    //     marginLayout.setAlignItems(FlexComponent.Alignment.CENTER);
    //     marginLayout.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
    //     marginLayout.getStyle().set("padding", "10px");
    //     marginLayout.getStyle().set("border-radius", "4px");
        
    //     // Action buttons
    //     Button actionButton = new Button(isBuy ? "BUY" : "SELL", e -> {
    //         // Add your trade execution logic here
    //         dialog.close();
    //     });
    //     actionButton.addThemeVariants(isBuy ? ButtonVariant.LUMO_PRIMARY : ButtonVariant.LUMO_ERROR);
    //     actionButton.setWidthFull();
        
    //     Button cancelButton = new Button("CANCEL", e -> dialog.close());
    //     cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    //     cancelButton.setWidthFull();
        
    //     HorizontalLayout buttons = new HorizontalLayout(actionButton, cancelButton);
    //     buttons.setWidthFull();
    //     buttons.setSpacing(true);
        
    //     // Layout everything
    //     VerticalLayout dialogLayout = new VerticalLayout(
    //         title,
    //         exchange,
    //         priceLayout,
    //         tradeType,
    //         fieldsLayout,
    //         marginLayout,
    //         buttons
    //     );
    //     dialogLayout.setPadding(true);
    //     dialogLayout.setSpacing(true);
    //     dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        
    //     dialog.add(dialogLayout);
    //     dialog.open();
    // }


    private void showTradeDialog(Stock stock, boolean isBuy) {
        Dialog dialog = new Dialog();
        dialog.setWidth("700px");
    
        // Header
        H2 title = new H2((isBuy ? "BUY " : "SELL ") + stock.getSymbol());
        title.getStyle().set("margin", "0");
        title.getStyle().set("font-size", "24px");
        title.getStyle().set("color", isBuy ? "var(--lumo-success-color)" : "var(--lumo-error-color)");
    
        // Input Fields
        TextField quantityField = new TextField("Qty");
        quantityField.setValueChangeMode(ValueChangeMode.EAGER);
        
        TextField priceField = new TextField("Price");
        priceField.setValueChangeMode(ValueChangeMode.EAGER);
        priceField.setValue(String.format("%.2f", stock.getPrice()));
    
        HorizontalLayout fieldsLayout = new HorizontalLayout(quantityField, priceField);
        fieldsLayout.setWidthFull();
    
        // Action Buttons
        Button actionButton = new Button(isBuy ? "BUY" : "SELL", e -> {
            if (isBuy) {
                sendBuyRequest(stock, quantityField.getValue(), priceField.getValue());
            }
            dialog.close();
        });
    
        actionButton.addThemeVariants(isBuy ? ButtonVariant.LUMO_PRIMARY : ButtonVariant.LUMO_ERROR);
        actionButton.setWidthFull();
    
        Button cancelButton = new Button("CANCEL", e -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.setWidthFull();
    
        HorizontalLayout buttons = new HorizontalLayout(actionButton, cancelButton);
        buttons.setWidthFull();
        buttons.setSpacing(true);
    
        // Layout everything
        VerticalLayout dialogLayout = new VerticalLayout(
            title,
            fieldsLayout,
            buttons
        );
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
    
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void sendBuyRequest(Stock stock, String quantity, String price) {
        Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
    
        if (userId == null || userId == -1) {
            Notification.show("User is not logged in.");
            return;
        }
    
        try {
            // Prepare request payload
            Map<String, Object> request = new HashMap<>();
            request.put("userId", userId);
    
            List<Map<String, Object>> stocks = new ArrayList<>();
            Map<String, Object> stockData = new HashMap<>();
            stockData.put("symbol", stock.getSymbol());
            stockData.put("quantity", Integer.parseInt(quantity));
            stockData.put("price", Double.parseDouble(price));
            stocks.add(stockData);
    
            request.put("stocks", stocks);
    
            // Send POST request
            String url = "http://localhost:8080/addToOwnedStock";
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
    
            if (response.getStatusCode().is2xxSuccessful()) {
                Notification.show("Stock added to holdings successfully!");
            } else {
                Notification.show("Failed to add stock to holdings.");
            }
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage());
        }
    }

    private void showAddFundDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("800px");
        dialog.setHeight("450px");
    
        // Create the form layout
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);
        dialogLayout.getStyle().set("background-color", "#E6EAF5");
    
        // Name field
        TextField nameField = new TextField("Enter Name Here :");
        nameField.setWidthFull();
        nameField.getStyle()
                .set("background-color", "white")
                .set("border-radius", "4px")
                .set("padding", "5px");
    
        // Amount field
        TextField amountField = new TextField("Enter Amount for fund:");
        amountField.setWidthFull();
        amountField.setValue("200");
        amountField.setPlaceholder("Margin Needed : ₹ 200");
        amountField.getStyle()
                .set("background-color", "white")
                .set("border-radius", "4px")
                .set("padding", "5px");
    
        // Payment option combo box
        ComboBox<String> paymentOption = new ComboBox<>("Choose Payment Option :");
        paymentOption.setItems("Bank Transfer", "UPI", "Credit/Debit Card", "Net Banking");
        paymentOption.setPlaceholder("Select here");
        paymentOption.setWidthFull();
        paymentOption.getStyle()
                .set("background-color", "white")
                .set("border-radius", "4px")
                .set("padding", "5px");
    
        // Buttons layout
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        buttonLayout.getStyle().set("margin-top", "20px");
    
        // Pay button
        Button payButton = new Button("Pay");
        payButton.getStyle()
                .set("background-color", "#22C55E")
                .set("color", "white")
                .set("border-radius", "4px")
                .set("flex", "1");
        payButton.setWidthFull();
        payButton.addClickListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getValue());
                if (amount < 200) {
                    Notification.show("Amount must be at least ₹200", 3000, Notification.Position.MIDDLE);
                } else {
                    // Send request to backend
                    sendAddFundRequest(amount);
                    updateWelcomeMessage(nameField.getValue());
                    updateFundsDisplay(amount);
                    dialog.close();
                }
            } catch (NumberFormatException ex) {
                Notification.show("Please enter a valid amount", 3000, Notification.Position.MIDDLE);
            }
        });
    
        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.getStyle()
                .set("background-color", "#EF4444")
                .set("color", "white")
                .set("border-radius", "4px")
                .set("flex", "1");
        cancelButton.setWidthFull();
        cancelButton.addClickListener(e -> dialog.close());
    
        // Add buttons to the button layout
        buttonLayout.add(payButton, cancelButton);
    
        // Add all components to the dialog layout
        dialogLayout.add(
            nameField,
            amountField,
            paymentOption,
            buttonLayout
        );
    
        dialog.add(dialogLayout);
        dialog.open();
    }


    private void showWithdrawDialog(double availableAmount) {
        Dialog dialog = new Dialog();
        dialog.setWidth("800px");
        dialog.setHeight("400px");
    
        // Create the form layout
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);
        dialogLayout.getStyle().set("background-color", "#E6EAF5");
    
        // Available balance display
        H2 balanceTitle = new H2("Available Balance: ₹" + availableAmount);
        balanceTitle.getStyle().set("color", "#333");
    
        // Amount field
        TextField amountField = new TextField("Enter Amount to Withdraw:");
        amountField.setWidthFull();
        amountField.getStyle()
                .set("background-color", "white")
                .set("border-radius", "4px")
                .set("padding", "5px");
    
        // Withdrawal method combo box
        ComboBox<String> withdrawalMethod = new ComboBox<>("Choose Withdrawal Method:");
        withdrawalMethod.setItems("Bank Transfer", "UPI");
        withdrawalMethod.setPlaceholder("Select method");
        withdrawalMethod.setWidthFull();
        withdrawalMethod.getStyle()
                .set("background-color", "white")
                .set("border-radius", "4px")
                .set("padding", "5px");
    
        // Buttons layout
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        buttonLayout.getStyle().set("margin-top", "20px");
    
        // Withdraw button
        Button withdrawButton = new Button("Withdraw");
        withdrawButton.getStyle()
                .set("background-color", "#22C55E")
                .set("color", "white")
                .set("border-radius", "4px")
                .set("flex", "1");
        withdrawButton.setWidthFull();
        withdrawButton.addClickListener(e -> {
            try {
                double withdrawAmount = Double.parseDouble(amountField.getValue());
                if (withdrawAmount <= 0) {
                    Notification.show("Please enter a valid amount", 3000, Notification.Position.MIDDLE);
                } else if (withdrawAmount > availableAmount) {
                    Notification.show("Insufficient funds", 3000, Notification.Position.MIDDLE);
                } else if ((availableAmount - withdrawAmount) < 200) {
                    Notification.show("Cannot withdraw below margin requirement of ₹200", 3000, Notification.Position.MIDDLE);
                } else {
                    // Send request to backend
                    sendWithdrawRequest(withdrawAmount);
                    updateFundsDisplay(availableAmount - withdrawAmount);
                    dialog.close();
                }
            } catch (NumberFormatException ex) {
                Notification.show("Please enter a valid amount", 3000, Notification.Position.MIDDLE);
            }
        });
    
        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.getStyle()
                .set("background-color", "#EF4444")
                .set("color", "white")
                .set("border-radius", "4px")
                .set("flex", "1");
        cancelButton.setWidthFull();
        cancelButton.addClickListener(e -> dialog.close());
    
        // Add buttons to the button layout
        buttonLayout.add(withdrawButton, cancelButton);
    
        // Add all components to the dialog layout
        dialogLayout.add(
            balanceTitle,
            amountField,
            withdrawalMethod,
            buttonLayout
        );
    
        dialog.add(dialogLayout);
        dialog.open();
    }

    public void sendAddFundRequest(double amount) {
        try {
            int userId = getCurrentUserId(); // Implement this method to get the current user's ID
            String url = "http://localhost:8080/addFunds"; // Adjust this URL to match your backend
    
            // Create JSON payload
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("amount", amount);
    
            // Create HttpClient instance
            HttpClient httpClient = HttpClient.newHttpClient();
    
            // Create request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString(), StandardCharsets.UTF_8))
                    .build();
    
            // Send request asynchronously
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(responseBody -> Notification.show("Funds added successfully", 3000, Notification.Position.MIDDLE))
                    .exceptionally(error -> {
                        Notification.show("Error occurred: " + error.getMessage(), 3000, Notification.Position.MIDDLE);
                        return null;
                    });
        } catch (Exception e) {
            Notification.show("Error occurred: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void sendWithdrawRequest(double amount) {
        try {
            int userId = getCurrentUserId();
            String url = "http://localhost:8080/withdrawFunds";
    
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("amount", amount);
    
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString(), StandardCharsets.UTF_8))
                    .build();
    
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(responseBody -> Notification.show("Withdrawal successful", 3000, Notification.Position.MIDDLE))
                    .exceptionally(error -> {
                        Notification.show("Error occurred: " + error.getMessage(), 3000, Notification.Position.MIDDLE);
                        return null;
                    });
        } catch (Exception e) {
            Notification.show("Error occurred: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private int getCurrentUserId() {
    Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");

    if (userId != null) {
        System.out.println("User ID: " + userId); 
        return userId; 
    } else {
        System.out.println("User is not logged in.");
        return -1; // Return -1 to indicate no user is logged in
    }
}

    private void updateWelcomeMessage(String userName) {
        H2 welcomeText = (H2) getContent().getChildren()
                .filter(component -> component instanceof H2)
                .findFirst()
                .orElse(null);
        
        if (welcomeText != null) {
            welcomeText.setText("Hi, " + userName + "!");
        }
    }

    private void updateFundsDisplay(double amount) {
        VerticalLayout fundsSection = (VerticalLayout) getContent().getChildren()
                .filter(component -> component instanceof VerticalLayout)
                .findFirst()
                .orElse(null);
        
        if (fundsSection != null) {
            fundsSection.removeAll();
            
            H2 availableAmount = new H2("Available Amount: ₹" + amount);
            H2 usedAmount = new H2("Used Amount: ₹0");
            
            fundsSection.add(availableAmount, usedAmount);
            
            // Re-add the buttons
            HorizontalLayout buttonsLayout = new HorizontalLayout();
            buttonsLayout.addClassName("funds-buttons");
            buttonsLayout.setSpacing(true);
    
            Button addFundButton = new Button("Add Fund", new Icon(VaadinIcon.PLUS));
            addFundButton.addClassName("add-fund-button");
            addFundButton.addClickListener(e -> showAddFundDialog());
    
            Button withdrawFundButton = new Button("Withdraw Fund", new Icon(VaadinIcon.MINUS));
            withdrawFundButton.addClassName("withdraw-fund-button");
            // Enable withdraw button only if amount is above margin requirement
            withdrawFundButton.setEnabled(amount > 200);
            withdrawFundButton.addClickListener(e -> showWithdrawDialog(amount));
    
            buttonsLayout.add(addFundButton, withdrawFundButton);
            fundsSection.add(buttonsLayout);
        }
    }
    

    private void createMainContent() {
        // Main content layout
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.addClassName("main-content");

        // Welcome section
        String userName = (String) VaadinSession.getCurrent().getAttribute("userName");
        if (userName == null) {
            userName = "Guest"; // Default value if not found
        }
        H2 welcomeText = new H2("Hi, " + userName + " !");
        welcomeText.addClassName("welcome-text");
        mainContent.add(welcomeText);

        // Funds section
        createFundsSection(mainContent);

        // Set main content
        setContent(mainContent);
    }

    private void createFundsSection(VerticalLayout mainContent) {
        VerticalLayout fundsSection = new VerticalLayout();
        fundsSection.addClassName("funds-section");
        fundsSection.setAlignItems(FlexComponent.Alignment.CENTER);
        fundsSection.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // No investments message
        H2 noInvestmentsText = new H2("Ohh, no investments yet!!!");
        noInvestmentsText.addClassName("no-investments-text");

        Paragraph getStartedText = new Paragraph("Get started by adding fund");
        getStartedText.addClassName("get-started-text");

        // Buttons container
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addClassName("funds-buttons");
        buttonsLayout.setSpacing(true);

        Button addFundButton = new Button("Add Fund", new Icon(VaadinIcon.PLUS));
        addFundButton.addClassName("add-fund-button");
        addFundButton.addClickListener(e -> showAddFundDialog());

        Button withdrawFundButton = new Button("Withdraw Fund", new Icon(VaadinIcon.MINUS));
        withdrawFundButton.addClassName("withdraw-fund-button");
        withdrawFundButton.setEnabled(false); // Disabled by default
        withdrawFundButton.addClickListener(e -> {
            // Withdraw fund logic here
        });

        buttonsLayout.add(addFundButton, withdrawFundButton);

        fundsSection.add(noInvestmentsText, getStartedText, buttonsLayout);
        mainContent.add(fundsSection);
    }

    private Tab createTab(String text, VaadinIcon icon, String route) {
        Icon tabIcon = icon.create();
        tabIcon.addClassName("tab-icon");

        Span tabText = new Span(text);
        HorizontalLayout tabContent = new HorizontalLayout(tabIcon, tabText);
        tabContent.setSpacing(true);
        tabContent.setAlignItems(FlexComponent.Alignment.CENTER);

        Tab tab = new Tab(tabContent);
        tab.addClassName("nav-tab");
        tab.getElement().addEventListener("click", e -> {
            getUI().ifPresent(ui -> ui.navigate(route));
        });
        return tab;
    }
    private static class Stock {
        private String symbol;
        private double price;
        private double change;

        public Stock(String symbol, double price, double change) {
            this.symbol = symbol;
            this.price = price;
            this.change = change;
        }

        public String getSymbol() {
            return symbol;
        }

        public double getPrice() {
            return price;
        }

        public double getChange() {
            return change;
        }
    }
}

