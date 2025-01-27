package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
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

@Route("orders")
@PageTitle("Orders | TradeWise")
public class OrdersView extends AppLayout {

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
            createTab("Funds", VaadinIcon.WALLET, "funds")
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


        // Orders section
        createOrdersSection(mainContent);

        // Set main content
        setContent(mainContent);
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
        getStartedButton.addClickListener(e -> {
            // Add logic for "Get Started" action
            getStartedButton.getUI().ifPresent(ui -> ui.navigate("place-order"));
        });

        ordersSection.add(ordersTitle, noOrdersMessage, getStartedButton);
        ordersSection.setAlignItems(FlexComponent.Alignment.CENTER);
        mainContent.add(ordersSection);
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

