package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.icon.Icon;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.vaadin.example.service.WatchlistService;

import java.util.ArrayList;
import java.util.List;

@Route("wishlist")
@PageTitle("Add to Wishlist | TradeWise")
public class WishlistView extends AppLayout {
    private Grid<Stock> grid;
    private List<Stock> selectedStocks = new ArrayList<>();
    private final WatchlistService watchlistService;

    @Autowired
    public WishlistView(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
        createHeader();
        createContent();
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

    private void createContent() {
        VerticalLayout content = new VerticalLayout();
        content.addClassName("wishlist-view");
        content.setSizeFull();
        
        H2 header = new H2("Add to Wishlist");
        header.addClassName("wishlist-header");
        
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search for stocks");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addClassName("search-field");
        searchField.setWidth("100%");
        
        grid = new Grid<>();
        grid.addClassName("stocks-grid");
        
        grid.addColumn(Stock::getSymbol).setHeader("Symbol").setAutoWidth(true);
        grid.addColumn(stock -> String.format("%.2f", stock.getPrice()))
            .setHeader("Price")
            .setAutoWidth(true);
            
        grid.addColumn(new ComponentRenderer<>(stock -> {
            HorizontalLayout changeLayout = new HorizontalLayout();
            changeLayout.setSpacing(true);
            changeLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            Icon arrow;
            String changeClass;
            double changePercentage;
            try {
                String percentChange = stock.getPercentChange(); // e.g., "+3.65%"
                String percentChangeReplaced = percentChange.replace("%", ""); // Removes the %

                changePercentage = Double.parseDouble(percentChangeReplaced);
            } catch (NumberFormatException | NullPointerException e) {
                changePercentage = 0.0; // Default value if parsing fails
            }

            if( (changePercentage) >= 0) {
                arrow = VaadinIcon.ARROW_UP.create();
                arrow.setColor("var(--lumo-success-color)");
                changeClass = "change-positive";
            } else {
                arrow = VaadinIcon.ARROW_DOWN.create();
                arrow.setColor("var(--lumo-error-color)");
                changeClass = "change-negative";
            }
            String percentChange = stock.getPercentChange(); // e.g., "+3.65%"
            String percentChangeReplaced = percentChange.replace("%", ""); // Removes the %
            Span changeValue = new Span(String.format("%.2f%%", Double.parseDouble(percentChangeReplaced)));

            changeValue.addClassName(changeClass);

            changeLayout.add(arrow, changeValue);
            return changeLayout;
        })).setHeader("Change").setAutoWidth(true);
        
        grid.addColumn(new ComponentRenderer<>(stock -> {
            RadioButtonGroup<String> radio = new RadioButtonGroup<>();
            radio.setItems("Add");
            radio.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    if (!selectedStocks.contains(stock)) {
                        selectedStocks.add(stock);
                    }
                } else {
                    selectedStocks.remove(stock);
                }
            });
            return radio;
        })).setHeader("Add to Wishlist").setAutoWidth(true);
        
        Button addToWishlistButton = new Button("Add to Wishlist");
        addToWishlistButton.setIcon(new Icon(VaadinIcon.PLUS));
        addToWishlistButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addToWishlistButton.addClickListener(e -> {
            if (!selectedStocks.isEmpty()) {
                for (Stock stock : selectedStocks) {
                    watchlistService.addToWatchlist(stock);
                }
                Notification.show("Added " + selectedStocks.size() + " stocks to watchlist");
                selectedStocks.clear();
                grid.getDataProvider().refreshAll();
            } else {
                Notification.show("Please select stocks to add to wishlist");
            }
        });
        
        loadStocks();
        
        content.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, header);
        content.add(
            header,
            searchField,
            grid,
            addToWishlistButton
        );

        setContent(content);
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

    private void loadStocks() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Stock[] stocks = restTemplate.getForObject("http://localhost:8080/getAllStocks", Stock[].class);
            grid.setItems(stocks);
            System.out.println(stocks[0].toString());
        } catch (Exception e) {
            Notification.show("Error loading stocks: " + e.getMessage());
        }
    }
}

