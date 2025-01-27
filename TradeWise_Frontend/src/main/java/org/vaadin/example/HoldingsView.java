package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;  // Add this import
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
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route("holdings")
@PageTitle("Holdings | TradeWise")
public class HoldingsView extends AppLayout {

    // Create a class to represent a holding row
    private static class HoldingItem {
        private String instrument;
        private int quantity;
        private double avgCost;
        private double ltp;
        private double pnl;
        private double netChange;
        private double dayChange;

        // Empty constructor for Grid
        public HoldingItem() {}

        // Getters
        public String getInstrument() { return instrument; }
        public int getQuantity() { return quantity; }
        public double getAvgCost() { return avgCost; }
        public double getLtp() { return ltp; }
        public double getPnl() { return pnl; }
        public double getNetChange() { return netChange; }
        public double getDayChange() { return dayChange; }
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
            createTab("Funds", VaadinIcon.WALLET, "funds")
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

        // Chart placeholder
        Div chartPlaceholder = new Div();
        chartPlaceholder.addClassName("chart-placeholder");
        chartPlaceholder.setText("Chart will be displayed here");

        // Add components to drawer
        addToDrawer(new VerticalLayout(searchField, watchlist, chartPlaceholder));
    }

    private void createMainContent() {
        // Main content layout
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.addClassName("main-content");

        // Welcome section
        H2 welcomeText = new H2("Hi, User!");
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

        H3 holdingsTitle = new H3("Holdings");
        holdingsTitle.addClassName("section-title");
        holdingsSection.add(holdingsTitle);

        // Create holdings grid with the HoldingItem class
        Grid<HoldingItem> grid = new Grid<>(HoldingItem.class, false);
        grid.addClassName("holdings-grid");

        // Add columns
        grid.addColumn(HoldingItem::getInstrument)
            .setHeader("Instrument")
            .setAutoWidth(true)
            .setFlexGrow(1);
            
        grid.addColumn(HoldingItem::getQuantity)
            .setHeader("Qty")
            .setAutoWidth(true);
            
        grid.addColumn(HoldingItem::getAvgCost)
            .setHeader("Avg cost")
            .setAutoWidth(true);
            
        grid.addColumn(HoldingItem::getLtp)
            .setHeader("LTP")
            .setAutoWidth(true);
            
        grid.addColumn(HoldingItem::getPnl)
            .setHeader("P&L")
            .setAutoWidth(true);
            
        grid.addColumn(HoldingItem::getNetChange)
            .setHeader("Net chg")
            .setAutoWidth(true);
            
        grid.addColumn(HoldingItem::getDayChange)
            .setHeader("Day chg")
            .setAutoWidth(true);

        // Style the grid
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setAllRowsVisible(true); // This replaces setHeightByRows

        holdingsSection.add(grid);
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
}

