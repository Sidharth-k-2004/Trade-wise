package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
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
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
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
            createTab("Funds", VaadinIcon.WALLET, "funds")
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
}

