package org.vaadin.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
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

@Route("orders")
@PageTitle("Orders | TradeWise")
public class OrdersView extends AppLayout {
    private RestTemplate restTemplate = new RestTemplate();

    public OrdersView() {
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

        // Set the "Orders" tab as selected
        tabs.setSelectedIndex(1);

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

    // private void createDrawer() {
    //     // Search field
    //     TextField searchField = new TextField();
    //     searchField.setPlaceholder("Search your stocks");
    //     searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
    //     searchField.addClassName("search-field");

    //     // Watchlist container
    //     VerticalLayout watchlist = new VerticalLayout();
    //     watchlist.addClassName("watchlist");
    //     watchlist.add(new H3("Watchlist"));

    //     watchlist.add(createWatchlistTable());

    //     // Chart placeholder
    //     Div chartPlaceholder = new Div();
    //     chartPlaceholder.addClassName("chart-placeholder");
    //     chartPlaceholder.setText("Chart will be displayed here");

    //     // Add components to drawer
    //     addToDrawer(new VerticalLayout(searchField, watchlist, chartPlaceholder));
    // }


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

    // private List<Stock> createDummyStocks() {
    //     List<Stock> stocks = new ArrayList<>();
    //     stocks.add(new Stock("AAPL", 150.25, 1.5));
    //     stocks.add(new Stock("GOOGL", 2750.10, -0.5));
    //     stocks.add(new Stock("MSFT", 305.75, 0.8));
    //     stocks.add(new Stock("AMZN", 3300.50, -1.2));
    //     stocks.add(new Stock("TSLA", 750.80, 2.3));
    //     return stocks;
    // }

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

    private void createMainContent() {
        // Main content layout
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.addClassName("main-content");

        // Welcome section
        H2 welcomeText = new H2("Hi, User!");
        welcomeText.addClassName("welcome-text");
        mainContent.add(welcomeText);


        // Orders section
        createOrdersSection(mainContent);

        // Set main content
        setContent(mainContent);
    }

    private void showGetStartedDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("700px");
        
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setSpacing(false);
        dialogLayout.setPadding(false);
        dialogLayout.getStyle()
            .set("background-color", "#F8FAFF");
    
        // Search section with BSE/NSE options
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search for stocks");
        searchField.setWidthFull();
        searchField.getStyle()
            .set("--lumo-contrast-10pct", "#E8EFF5")
            .set("margin", "10");
    
        // BSE/NSE radio group
        RadioButtonGroup<String> exchangeGroup = new RadioButtonGroup<>();
        exchangeGroup.setItems("BSE", "NSE");
        exchangeGroup.setValue("BSE");
        exchangeGroup.setThemeName("horizontal");
        
        // Price displays
        Span bsePrice = new Span("₹ 232.75");
        Span nsePrice = new Span("₹ 232.75");
        
        HorizontalLayout priceLayout = new HorizontalLayout();
        priceLayout.setWidthFull();
        priceLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        
        HorizontalLayout bseLayout = new HorizontalLayout();
        bseLayout.add(new Radio("BSE"), bsePrice);
        bseLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        
        HorizontalLayout nseLayout = new HorizontalLayout();
        nseLayout.add(new Radio("NSE"), nsePrice);
        nseLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        
        priceLayout.add(bseLayout, nseLayout);
    
        // Trade type selection
        RadioButtonGroup<String> tradeType = new RadioButtonGroup<>();
        tradeType.setItems("Intraday", "Longterm");
        tradeType.setValue("Intraday");
        tradeType.setThemeName("horizontal");
        tradeType.getStyle().set("margin-top", "20px");
    
        // Input fields
        HorizontalLayout fieldsLayout = new HorizontalLayout();
        fieldsLayout.setWidthFull();
        fieldsLayout.setSpacing(true);
    
        TextField qtyField = new TextField();
        qtyField.setLabel("Qty");
        qtyField.setWidthFull();
    
        TextField priceField = new TextField();
        priceField.setLabel("Price");
        priceField.setWidthFull();
    
        TextField triggerField = new TextField();
        triggerField.setLabel("Trigger Price");
        triggerField.setWidthFull();
    
        fieldsLayout.add(qtyField, priceField, triggerField);
    
        // Margin section
        HorizontalLayout marginLayout = new HorizontalLayout();
        marginLayout.setWidthFull();
        marginLayout.getStyle()
            .set("background-color", "#EBEEF2")
            .set("padding", "15px")
            .set("border-radius", "4px")
            .set("margin-top", "20px");
    
        Span marginLabel = new Span("Margin required : ");
        Span marginAmount = new Span("₹ 232.75");
        marginLayout.add(marginLabel, marginAmount);
        marginLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    
        // Buttons
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        buttonLayout.getStyle().set("margin-top", "20px");
    
        Button buyButton = new Button("BUY");
        buyButton.getStyle()
            .set("background-color", "#6B8FF9")
            .set("color", "white")
            .set("border-radius", "4px")
            .set("width", "50%");
    
        Button cancelButton = new Button("CANCEL");
        cancelButton.getStyle()
            .set("background-color", "white")
            .set("color", "#1A1A1A")
            .set("border", "1px solid #E5E7EB")
            .set("border-radius", "4px")
            .set("width", "50%");
        
        cancelButton.addClickListener(e -> dialog.close());
        
        buttonLayout.add(buyButton, cancelButton);
    
        // Add all components to dialog
        dialogLayout.add(
            searchField,
            priceLayout,
            tradeType,
            fieldsLayout,
            marginLayout,
            buttonLayout
        );
    
        dialog.add(dialogLayout);
        dialog.open();
    }

private void createOrdersSection(VerticalLayout mainContent) {
    VerticalLayout ordersSection = new VerticalLayout();
    ordersSection.addClassName("orders-section");

    H3 ordersTitle = new H3("Orders");
    ordersTitle.addClassName("section-title");

    Div noOrdersMessage = new Div();
    noOrdersMessage.setText("You haven't placed any orders");
    noOrdersMessage.addClassName("no-orders-message");

    Button getStartedButton = new Button("Get Started");
    getStartedButton.addClassName("get-started-button");
    getStartedButton.addClickListener(e -> showGetStartedDialog());

    ordersSection.add(ordersTitle, noOrdersMessage, getStartedButton);
    ordersSection.setAlignItems(FlexComponent.Alignment.CENTER);
    mainContent.add(ordersSection);
}


private static class Radio extends Div {
    public Radio(String label) {
        getStyle()
            .set("width", "20px")
            .set("height", "20px")
            .set("border", "2px solid #6B8FF9")
            .set("border-radius", "50%")
            .set("margin-right", "8px")
            .set("cursor", "pointer");
        
        Span labelSpan = new Span(label);
        labelSpan.getStyle().set("margin-left", "8px");
        
        add(labelSpan);
    }
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

