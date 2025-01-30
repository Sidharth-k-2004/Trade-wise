package org.vaadin.example;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
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

@Route("positions")
@PageTitle("Positions | TradeWise")
public class PositionsView extends AppLayout {

    // Create a class to represent a position row
    private static class PositionItem {
        private String product;
        private String instrument;
        private int quantity;
        private double avg;
        private double ltp;
        private double pnl;
        private double change;

        // Empty constructor for Grid
        public PositionItem() {}

        // Getters
        public String getProduct() { return product; }
        public String getInstrument() { return instrument; }
        public int getQuantity() { return quantity; }
        public double getAvg() { return avg; }
        public double getLtp() { return ltp; }
        public double getPnl() { return pnl; }
        public double getChange() { return change; }
    }

    public PositionsView() {
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
        
        // Set Positions tab as selected
        tabs.setSelectedIndex(3);

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
        // Search field
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search your stocks");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.addClassName("search-field");

        // Watchlist container
        VerticalLayout watchlist = new VerticalLayout();
        watchlist.addClassName("watchlist");
        watchlist.add(new H3("Watchlist"));

        watchlist.add(createWatchlistTable());

        // Chart placeholder
        Div chartPlaceholder = new Div();
        chartPlaceholder.addClassName("chart-placeholder");
        chartPlaceholder.setText("Chart will be displayed here");

        // Add components to drawer
        addToDrawer(new VerticalLayout(searchField, watchlist, chartPlaceholder));
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

        grid.setItems(createDummyStocks());
        grid.addClassName("watchlist-table");

        return grid;
    }

    private List<Stock> createDummyStocks() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock("AAPL", 150.25, 1.5));
        stocks.add(new Stock("GOOGL", 2750.10, -0.5));
        stocks.add(new Stock("MSFT", 305.75, 0.8));
        stocks.add(new Stock("AMZN", 3300.50, -1.2));
        stocks.add(new Stock("TSLA", 750.80, 2.3));
        return stocks;
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

    private void createMainContent() {
        // Main content layout
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.addClassName("main-content");

        // Welcome section
        H2 welcomeText = new H2("Hi, User!");
        welcomeText.addClassName("welcome-text");
        mainContent.add(welcomeText);

        // Positions section
        createPositionsSection(mainContent);

        // Set main content
        setContent(mainContent);
    }

    private void createPositionsSection(VerticalLayout mainContent) {
        VerticalLayout positionsSection = new VerticalLayout();
        positionsSection.addClassName("positions-section");

        H3 positionsTitle = new H3("Positions");
        positionsTitle.addClassName("section-title");
        positionsSection.add(positionsTitle);

        // Create positions grid with the PositionItem class
        Grid<PositionItem> grid = new Grid<>(PositionItem.class, false);
        grid.addClassName("positions-grid");

        // Add columns
        grid.addColumn(PositionItem::getProduct)
            .setHeader("Product")
            .setAutoWidth(true)
            .setFlexGrow(1);
            
        grid.addColumn(PositionItem::getInstrument)
            .setHeader("Instrument")
            .setAutoWidth(true)
            .setFlexGrow(1);
            
        grid.addColumn(PositionItem::getQuantity)
            .setHeader("Qty")
            .setAutoWidth(true);
            
        grid.addColumn(PositionItem::getAvg)
            .setHeader("Avg")
            .setAutoWidth(true);
            
        grid.addColumn(PositionItem::getLtp)
            .setHeader("LTP")
            .setAutoWidth(true);
            
        grid.addColumn(PositionItem::getPnl)
            .setHeader("P&L")
            .setAutoWidth(true);
            
        grid.addColumn(PositionItem::getChange)
            .setHeader("Chg")
            .setAutoWidth(true);

        // Style the grid
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setAllRowsVisible(true);

        positionsSection.add(grid);
        mainContent.add(positionsSection);
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

