// package org.vaadin.example;

// import com.vaadin.flow.component.applayout.AppLayout;
// import com.vaadin.flow.component.applayout.DrawerToggle;
// import com.vaadin.flow.component.button.Button;
// import com.vaadin.flow.component.html.Div;
// import com.vaadin.flow.component.html.H2;
// import com.vaadin.flow.component.html.H3;
// import com.vaadin.flow.component.html.Image;
// import com.vaadin.flow.component.html.Span;
// import com.vaadin.flow.component.icon.Icon;
// import com.vaadin.flow.component.icon.VaadinIcon;
// import com.vaadin.flow.component.orderedlayout.FlexComponent;
// import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
// import com.vaadin.flow.component.orderedlayout.VerticalLayout;
// import com.vaadin.flow.component.tabs.Tab;
// import com.vaadin.flow.component.tabs.Tabs;
// import com.vaadin.flow.component.textfield.TextField;
// import com.vaadin.flow.router.PageTitle;
// import com.vaadin.flow.router.Route;
// import com.vaadin.flow.server.StreamResource;

// @Route("dashboard")
// @PageTitle("Dashboard | TradeWise")
// public class DashboardView extends AppLayout {

//     public DashboardView() {
//         createHeader();
//         createDrawer();
//         createMainContent();
//     }

//     private void createHeader() {
//         // Logo
//         StreamResource logoResource = new StreamResource("logo.png",
//                 () -> getClass().getResourceAsStream("/images/logo.png"));
//         Image logo = new Image(logoResource, "TradeWise");
//         logo.setHeight("80px");
//         logo.setWidth("180px");
//         logo.addClassName("logo");

//         // Navigation tabs
//         Tabs tabs = new Tabs(
//             createTab("Dashboard", VaadinIcon.DASHBOARD),
//             createTab("Orders", VaadinIcon.CART),
//             createTab("Holdings", VaadinIcon.CHART),  // Changed from PORTFOLIO
//             createTab("Positions", VaadinIcon.TRENDING_UP),
//             createTab("Funds", VaadinIcon.WALLET)
//         );
//         tabs.addClassName("navigation-tabs");

//         // Profile button
//         Button profileButton = new Button(new Icon(VaadinIcon.USER));
//         profileButton.addClassName("profile-button");

//         // Header layout
//         HorizontalLayout header = new HorizontalLayout(
//             new DrawerToggle(),
//             logo,
//             tabs,
//             profileButton
//         );
//         header.addClassName("header");
//         header.setWidthFull();
//         header.setHeight("120px");
//         header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
//         header.setAlignItems(FlexComponent.Alignment.CENTER);

//         addToNavbar(header);
//     }

//     private void createDrawer() {
//         // Search field
//         TextField searchField = new TextField();
//         searchField.setPlaceholder("Search your stocks");
//         searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
//         searchField.addClassName("search-field");

//         // Watchlist container
//         VerticalLayout watchlist = new VerticalLayout();
//         watchlist.addClassName("watchlist");
//         watchlist.add(new H3("Watchlist"));

//         // Chart placeholder
//         Div chartPlaceholder = new Div();
//         chartPlaceholder.addClassName("chart-placeholder");
//         chartPlaceholder.setText("Chart will be displayed here");

//         // Add components to drawer
//         addToDrawer(new VerticalLayout(searchField, watchlist, chartPlaceholder));
//     }

//     private void createMainContent() {
//         // Main content layout
//         VerticalLayout mainContent = new VerticalLayout();
//         mainContent.addClassName("main-content");

//         // Welcome section
//         H2 welcomeText = new H2("Hi, User!");
//         welcomeText.addClassName("welcome-text");
//         mainContent.add(welcomeText);

//         // Equity section
//         createEquitySection(mainContent);

//         // Holdings section
//         createHoldingsSection(mainContent);

//         // Set main content
//         setContent(mainContent);
//     }

//     private void createEquitySection(VerticalLayout mainContent) {
//         VerticalLayout equitySection = new VerticalLayout();
//         equitySection.addClassName("equity-section");

//         H3 equityTitle = new H3("Equity");
//         equityTitle.addClassName("section-title");

//         Div equityValue = new Div();
//         equityValue.setText("₹ --");
//         equityValue.addClassName("equity-value");

//         Div marginInfo = new Div();
//         marginInfo.setText("Margin Available");
//         marginInfo.addClassName("margin-info");

//         equitySection.add(equityTitle, equityValue, marginInfo);
//         mainContent.add(equitySection);
//     }

//     private void createHoldingsSection(VerticalLayout mainContent) {
//         VerticalLayout holdingsSection = new VerticalLayout();
//         holdingsSection.addClassName("holdings-section");

//         H3 holdingsTitle = new H3("Holdings (0)");
//         holdingsTitle.addClassName("section-title");

//         holdingsSection.add(holdingsTitle);
//         mainContent.add(holdingsSection);
//     }

//     private Tab createTab(String text, VaadinIcon icon) {
//         Icon tabIcon = icon.create();
//         tabIcon.addClassName("tab-icon");
        
//         // Using Span instead of Text
//         Span tabText = new Span(text);
//         HorizontalLayout tabContent = new HorizontalLayout(tabIcon, tabText);
//         tabContent.setSpacing(true);
//         tabContent.setAlignItems(FlexComponent.Alignment.CENTER);
        
//         Tab tab = new Tab(tabContent);
//         tab.addClassName("nav-tab");
//         return tab;
//     }
// }







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
import org.vaadin.example.WatchlistComponent;

@Route("dashboard")
@PageTitle("Dashboard | TradeWise")
public class DashboardView extends AppLayout {

    public DashboardView() {
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
        // WatchlistComponent watchlist = new WatchlistComponent();
        // addToDrawer(new VerticalLayout(searchField, watchlist));
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

        // Equity section
        createEquitySection(mainContent);

        // Holdings section
        createHoldingsSection(mainContent);

        // Set main content
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
}







