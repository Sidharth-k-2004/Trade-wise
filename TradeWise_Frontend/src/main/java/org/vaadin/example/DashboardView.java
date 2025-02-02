 package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
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

import java.util.*;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.vaadin.example.service.WatchlistService;


@Route("dashboard")
@PageTitle("Dashboard | TradeWise")
public class DashboardView extends AppLayout {

    private final WatchlistService watchlistService;
     private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public DashboardView(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
        createHeader();
        createDrawer();
        createMainContent();
    }

    private void createHeader() {
        StreamResource logoResource = new StreamResource("logo.png",
                () -> getClass().getResourceAsStream("/images/logo.png"));
        Image logo = new Image(logoResource, "TradeWise");
        logo.setHeight("80px");
        logo.setWidth("180px");
        logo.addClassName("logo");

        Tabs tabs = new Tabs(
            createTab("Dashboard", VaadinIcon.DASHBOARD, "dashboard"),
            createTab("Orders", VaadinIcon.CART, "orders"),
            createTab("Holdings", VaadinIcon.CHART, "holdings"),
            createTab("Positions", VaadinIcon.TRENDING_UP, "positions"),
            createTab("Funds", VaadinIcon.WALLET, "funds"),
            createTab("Wishlist", VaadinIcon.HEART, "wishlist")
        );
        tabs.addClassName("navigation-tabs");

        Button profileButton = new Button(new Icon(VaadinIcon.USER));
        profileButton.addClassName("profile-button");
    
        ProfileMenu profileMenu = new ProfileMenu();
    
        profileButton.addClickListener(event -> {
            if (profileMenu.isOpened()) {
                profileMenu.close();
            } else {
                profileMenu.setOpened(true);
            }
        });

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
    //     TextField searchField = new TextField();
    //     searchField.setPlaceholder("Search your stocks");
    //     searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
    //     searchField.addClassName("search-field");

    //     VerticalLayout watchlist = new VerticalLayout();
    //     watchlist.addClassName("watchlist");
    //     watchlist.add(new H3("Watchlist"));

    //     watchlist.add(createWatchlistTable());

    //     Div chartPlaceholder = new Div();
    //     chartPlaceholder.addClassName("chart-placeholder");
    //     chartPlaceholder.setText("Chart will be displayed here");

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
            List<Map<String, Object>> stocks = fetchStocksFromApi(userId);
            if (!stocks.isEmpty()) {
                PortfolioDonutChart donutChart = new PortfolioDonutChart(stocks);
                drawer.add(searchField, watchlist, donutChart);
            } else {
                Notification.show("No stocks found in portfolio");
                drawer.add(searchField, watchlist);
            }
        } catch (Exception e) {
            Notification.show("Error loading portfolio data");
            drawer.add(searchField, watchlist);
        }
    } else {
        Notification.show("User not logged in");
        drawer.add(searchField, watchlist);
    }

    addToDrawer(drawer);
}



    private List<Map<String, Object>> fetchStocksFromApi(Integer userId) {
        String url = "http://localhost:8080/wishlist/" + userId;
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
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
            String percentChange = stock.getPercentChange(); 
            String percentChangeReplaced = percentChange.replace("%", ""); // Removes the %
            double percentChangeDouble = Double.parseDouble(percentChangeReplaced);
            Span change = new Span(String.format("%.2f%%", percentChangeDouble));
            change.getElement().getThemeList().add(Double.parseDouble(percentChangeReplaced) >= 0 ? "badge success" : "badge error");

            HorizontalLayout infoLayout = new HorizontalLayout(symbol, price, change);
            infoLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            Button buyButton = new Button("Buy", new Icon(VaadinIcon.PLUS));
            buyButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
            buyButton.getStyle().set("display", "none");
            buyButton.addClickListener(e -> showTradeDialog(stock, true));

            Button sellButton = new Button("Sell", new Icon(VaadinIcon.MINUS));
            sellButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            sellButton.getStyle().set("display", "none");
            sellButton.addClickListener(e -> showTradeDialog(stock, false));

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

    private void createMainContent() {
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.addClassName("main-content");

        H2 welcomeText = new H2("Hi, User!");
        welcomeText.addClassName("welcome-text");
        mainContent.add(welcomeText);

        createEquitySection(mainContent);
        createHoldingsSection(mainContent);

        setContent(mainContent);
    }

    private void createEquitySection(VerticalLayout mainContent) {
        VerticalLayout equitySection = new VerticalLayout();
        equitySection.addClassName("equity-section");

        H3 equityTitle = new H3("Equity");
        equityTitle.addClassName("section-title");

        Div equityValue = new Div();
        equityValue.setText("₹ --");
        equityValue.addClassName("equity-value");

        Div marginInfo = new Div();
        marginInfo.setText("Margin Available");
        marginInfo.addClassName("margin-info");

        equitySection.add(equityTitle, equityValue, marginInfo);
        mainContent.add(equitySection);
    }

    private void createHoldingsSection(VerticalLayout mainContent) {
        VerticalLayout holdingsSection = new VerticalLayout();
        holdingsSection.addClassName("holdings-section");

        H3 holdingsTitle = new H3("Holdings (0)");
        holdingsTitle.addClassName("section-title");

        holdingsSection.add(holdingsTitle);
        mainContent.add(holdingsSection);
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
    // private List<Stock> createDummyStocks() {
    //     List<Stock> stocks = new ArrayList<>();
    //     stocks.add(new Stock("AAPL", 150.25, 1.5, 1.0));
    //     stocks.add(new Stock("GOOGL", 2750.10, -0.5, -0.02));
    //     stocks.add(new Stock("MSFT", 305.75, 0.8, 0.26));
    //     stocks.add(new Stock("AMZN", 3300.50, -1.2, -0.04));
    //     stocks.add(new Stock("TSLA", 750.80, 2.3, 0.31));
    //     return stocks;
    // }
}

