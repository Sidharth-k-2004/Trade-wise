package org.vaadin.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;  // Add this import
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
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

@Route("holdings")
@PageTitle("Holdings | TradeWise")
public class HoldingsView extends AppLayout {
    private final RestTemplate restTemplate = new RestTemplate(); 
    
    private static class HoldingItem {
        private String stock;
        private int quantity;
        private double buyPrice;
        private double currentPrice;
        private double investment;
        private double currentValue;
        private double unrealizedPnL;
        private LocalDate purchaseDate;
        // Empty constructor for Grid
        public HoldingItem() {}


    
        // Getters
        public String getstock() { return stock; }
        public int getQuantity() { return quantity; }
        public double getbuyPrice() { return buyPrice; }
        public double getcurrentPrice() { return currentPrice; }
        public double getinvestment() { return investment; }
        public double getcurrentValue() { return currentValue; }
        public double getunrealizedPnL() { return unrealizedPnL; }
        public LocalDate getpurchaseDate(){return purchaseDate;}
    
        // Setters (added)
        public void setstock(String stock) { this.stock = stock; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public void setbuyPrice(double buyPrice) { this.buyPrice = buyPrice; }
        public void setcurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
        public void setinvestment(double investment) { this.investment = investment; }
        public void setcurrentValue(double currentValue) { this.currentValue = currentValue; }
        public void setunrealizedPnL(double unrealizedPnL) { this.unrealizedPnL = unrealizedPnL; }
        public void setpurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    }
    

    public HoldingsView() {
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
        
        // Set Holdings tab as selected
        tabs.setSelectedIndex(2);

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


    private void showTradeDialog(Stock stock, boolean isBuy) {
        Dialog dialog = new Dialog();
        dialog.setWidth("700px");
        
        // Header
        H2 title = new H2((isBuy ? "BUY " : "SELL ") + stock.getSymbol());
        title.getStyle().set("margin", "0");
        title.getStyle().set("font-size", "24px");
        title.getStyle().set("color", isBuy ? "var(--lumo-success-color)" : "var(--lumo-error-color)");
        
        // Exchange selection
        RadioButtonGroup<String> exchange = new RadioButtonGroup<>();
        exchange.setItems("BSE", "NSE");
        exchange.setValue("BSE");
        exchange.setThemeName("horizontal");
        
        // Price labels
        HorizontalLayout priceLayout = new HorizontalLayout();
        Span bsePrice = new Span("₹ " + String.format("%.2f", stock.getPrice()));
        Span nsePrice = new Span("₹ " + String.format("%.2f", stock.getPrice()));
        priceLayout.add(bsePrice, new Span(" | "), nsePrice);
        priceLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        
        // Trade type
        RadioButtonGroup<String> tradeType = new RadioButtonGroup<>();
        tradeType.setItems("Intraday", "Longterm");
        tradeType.setValue("Intraday");
        tradeType.setThemeName("horizontal");
        
        // Input fields
        TextField quantityField = new TextField("Qty");
        quantityField.setValueChangeMode(ValueChangeMode.EAGER);
        
        TextField priceField = new TextField("Price");
        priceField.setValueChangeMode(ValueChangeMode.EAGER);
        priceField.setValue(String.format("%.2f", stock.getPrice()));
        
        TextField triggerField = new TextField("Trigger Price");
        triggerField.setValueChangeMode(ValueChangeMode.EAGER);
        
        // Fields layout
        HorizontalLayout fieldsLayout = new HorizontalLayout(quantityField, priceField, triggerField);
        fieldsLayout.setWidthFull();
        
        // Margin required section
        HorizontalLayout marginLayout = new HorizontalLayout();
        Span marginLabel = new Span("Margin required : ");
        Span marginAmount = new Span("₹ " + String.format("%.2f", stock.getPrice()));
        marginLayout.add(marginLabel, marginAmount);
        marginLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        marginLayout.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
        marginLayout.getStyle().set("padding", "10px");
        marginLayout.getStyle().set("border-radius", "4px");
        
        // Action buttons
        Button actionButton = new Button(isBuy ? "BUY" : "SELL", e -> {
            // Add your trade execution logic here
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
            exchange,
            priceLayout,
            tradeType,
            fieldsLayout,
            marginLayout,
            buttons
        );
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        
        dialog.add(dialogLayout);
        dialog.open();
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

    private void createMainContent() {
        // Main content layout
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.addClassName("main-content");

        String userName = (String) VaadinSession.getCurrent().getAttribute("userName");
        if (userName == null) {
            userName = "Guest"; // Default value if not found
        }
        H2 welcomeText = new H2("Hi, " + userName + " !");
        welcomeText.addClassName("welcome-text");
        mainContent.add(welcomeText);

        // Holdings section
        createHoldingsSection(mainContent);

        // Set main content
        setContent(mainContent);
    }


    private void createHoldingsSection(VerticalLayout mainContent) {
    VerticalLayout holdingsSection = new VerticalLayout();
    holdingsSection.addClassName("holdings-section");

    Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");

    if (userId == null || userId == -1) {
        return;
    }

    H3 holdingsTitle = new H3("Holdings");
    holdingsTitle.addClassName("section-title");
    holdingsSection.add(holdingsTitle);

    // Create holdings grid
    Grid<HoldingItem> grid = new Grid<>(HoldingItem.class, false);
    grid.addClassName("holdings-grid");

    // Define columns
    grid.addColumn(HoldingItem::getstock).setHeader("Stock").setAutoWidth(true);
    grid.addColumn(HoldingItem::getQuantity).setHeader("Qty").setAutoWidth(true);
    grid.addColumn(HoldingItem::getbuyPrice).setHeader("Bought Price").setAutoWidth(true);
    grid.addColumn(HoldingItem::getinvestment) // Total Invested Price
        .setHeader("Total Invested Price").setAutoWidth(true);
    grid.addColumn(HoldingItem::getcurrentPrice).setHeader("Current Price").setAutoWidth(true);
    grid.addColumn(HoldingItem::getcurrentValue) // Total Holding Price
        .setHeader("Total Holding Price").setAutoWidth(true);
    grid.addColumn(HoldingItem::getunrealizedPnL).setHeader("P&L").setAutoWidth(true);
    grid.addColumn(HoldingItem::getpurchaseDate).setHeader("Purchase Date").setAutoWidth(true);

    // Apply grid styles
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    grid.setAllRowsVisible(true);

    // Fetch data and populate grid
    List<HoldingItem> holdings = fetchHoldings(userId);
    
    grid.setItems(holdings);

    holdingsSection.add(grid);
    mainContent.add(holdingsSection);
}

/**
 * Fetches holdings data from the backend for a given user.
 */
private List<HoldingItem> fetchHoldings(Integer userId) {
    String url = "http://localhost:8080/holdings/" + userId;

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<HoldingItem[]> response = restTemplate.getForEntity(url, HoldingItem[].class);
    if (response.getStatusCode() == HttpStatus.OK) {
        return Arrays.asList(response.getBody());
    }

    return new ArrayList<>();
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

