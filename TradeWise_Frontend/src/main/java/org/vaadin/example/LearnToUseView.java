// package org.vaadin.example;

// import com.vaadin.flow.component.html.H1;
// import com.vaadin.flow.component.html.H2;
// import com.vaadin.flow.component.html.Paragraph;
// import com.vaadin.flow.component.orderedlayout.VerticalLayout;
// import com.vaadin.flow.component.tabs.Tab;
// import com.vaadin.flow.component.tabs.Tabs;
// import com.vaadin.flow.component.icon.VaadinIcon;
// import com.vaadin.flow.component.icon.Icon;
// import com.vaadin.flow.component.button.Button;
// import com.vaadin.flow.router.Route;
// import com.vaadin.flow.router.PageTitle;
// import com.vaadin.flow.component.orderedlayout.FlexComponent;
// import com.vaadin.flow.component.html.Div;
// import com.vaadin.flow.component.button.ButtonVariant;
// import com.vaadin.flow.router.RouterLink;

// @Route("learn-to-use")
// @PageTitle("Learn to Use | TradeWise")
// public class LearnToUseView extends VerticalLayout {

//     private VerticalLayout content;

//     public LearnToUseView() {
//         setSizeFull();
//         setAlignItems(Alignment.CENTER);

//         H1 title = new H1("Learn to Use Our Trading Platform");
//         title.getStyle().set("margin-top", "2em");

//         Paragraph subtitle = new Paragraph("Master the art of trading with our comprehensive guide");
//         subtitle.getStyle().set("font-size", "1.2em").set("margin-bottom", "2em");

//         content = new VerticalLayout();
//         content.setWidth("100%");
//         content.setMaxWidth("800px");
//         content.setPadding(true);

//         add(title, subtitle, content);

//         // Set default content
//         setContent("overview");
//     }


//     private void setContent(String tabName) {
//         content.removeAll();
//         switch (tabName.toLowerCase()) {
//             case "overview":
//                 content.add(createOverviewContent());
//                 break;
//             case "dashboard":
//                 content.add(createFeatureContent("Dashboard", 
//                     "Your command center for market insights and portfolio overview.",
//                     VaadinIcon.DASHBOARD,
//                     new String[]{
//                         "View real-time market data and indices",
//                         "Check your watchlist for quick stock updates",
//                         "See your portfolio summary and performance",
//                         "Access quick trade options for your favorite stocks"
//                     },
//                     "dashboard"));
//                 break;
//             case "orders":
//                 content.add(createFeatureContent("Orders", 
//                     "Place and manage your trades with ease.",
//                     VaadinIcon.CART,
//                     new String[]{
//                         "Place market, limit, and stop orders",
//                         "View and modify pending orders",
//                         "Check order history and execution details",
//                         "Set up advanced order types like brackets and covers"
//                     },
//                     "orders"));
//                 break;
//             case "holdings":
//                 content.add(createFeatureContent("Holdings", 
//                     "Track and manage your long-term investments.",
//                     VaadinIcon.BRIEFCASE,
//                     new String[]{
//                         "View all your current stock holdings",
//                         "Check individual stock performance",
//                         "Analyze your overall portfolio returns",
//                         "Easily sell holdings or add to existing positions"
//                     },
//                     "holdings"));
//                 break;
//             case "positions":
//                 content.add(createFeatureContent("Positions", 
//                     "Monitor and manage your intraday trades and F&O positions.",
//                     VaadinIcon.TRENDING_UP,
//                     new String[]{
//                         "Track open positions for stocks and derivatives",
//                         "View profit/loss for each position",
//                         "Square off or convert positions as needed",
//                         "Analyze position-wise and overall P&L"
//                     },
//                     "positions"));
//                 break;
//             case "funds":
//                 content.add(createFeatureContent("Funds", 
//                     "Manage your trading capital efficiently.",
//                     VaadinIcon.WALLET,
//                     new String[]{
//                         "View your available balance and used margin",
//                         "Add funds to your account (simulated)",
//                         "Withdraw funds from your account (simulated)",
//                         "Check detailed ledger for all fund movements"
//                     },
//                     "funds"));
//                 break;
//         }
//     }

//     private VerticalLayout createOverviewContent() {
//         VerticalLayout layout = new VerticalLayout();
//         layout.setAlignItems(Alignment.CENTER);

//         H2 welcomeTitle = new H2("Welcome to Our Trading Platform");
//         Paragraph welcomeDescription = new Paragraph("Learn how to navigate and use our powerful tools");

//         VerticalLayout featuresLayout = new VerticalLayout();
//         featuresLayout.setWidth("100%");

//         String[] features = {"Dashboard", "Orders", "Holdings", "Positions", "Funds"};
//         for (String feature : features) {
//             Button featureButton = new Button(feature, getIconForFeature(feature));
//             featureButton.setWidth("100%");
//             featureButton.getStyle()
//                 .set("justify-content", "space-between")
//                 .set("background-color", "var(--lumo-contrast-5pct)")
//                 .set("margin-bottom", "10px");
//             featureButton.addClickListener(e -> setContent(feature));
//             featuresLayout.add(featureButton);
//         }

//         layout.add(welcomeTitle, welcomeDescription, featuresLayout);
//         return layout;
//     }

//     private VerticalLayout createFeatureContent(String title, String description, VaadinIcon icon, String[] steps, String route) {
//         VerticalLayout layout = new VerticalLayout();
//         layout.setAlignItems(Alignment.START);

//         H2 featureTitle = new H2(title);
//         featureTitle.add(icon.create());

//         Paragraph featureDescription = new Paragraph(description);

//         H2 howToUseTitle = new H2("How to use:");
//         VerticalLayout stepsLayout = new VerticalLayout();
//         for (int i = 0; i < steps.length; i++) {
//             Paragraph step = new Paragraph((i + 1) + ". " + steps[i]);
//             stepsLayout.add(step);
//         }

//         Button goBackButton = new Button("Go Back", VaadinIcon.ARROW_LEFT.create());
//         goBackButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//         goBackButton.addClickListener(e -> setContent("overview"));

//         Button tryItNowButton = new Button("Try it now");
//         tryItNowButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//         tryItNowButton.addClickListener(e -> tryItNowButton.getUI().ifPresent(ui -> ui.navigate(route)));

//         Div buttonContainer = new Div(goBackButton, tryItNowButton);
//         buttonContainer.getStyle().set("display", "flex").set("justify-content", "space-between").set("width", "100%");

//         layout.add(featureTitle, featureDescription, howToUseTitle, stepsLayout, buttonContainer);
//         return layout;
//     }

//     private Icon getIconForFeature(String feature) {
//         switch (feature.toLowerCase()) {
//             case "dashboard":
//                 return VaadinIcon.DASHBOARD.create();
//             case "orders":
//                 return VaadinIcon.CART.create();
//             case "holdings":
//                 return VaadinIcon.BRIEFCASE.create();
//             case "positions":
//                 return VaadinIcon.TRENDING_UP.create();
//             case "funds":
//                 return VaadinIcon.WALLET.create();
//             default:
//                 return VaadinIcon.INFO_CIRCLE.create();
//         }
//     }
// }





package org.vaadin.example;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.UI;

@Route("learn-to-use")
@PageTitle("Learn to Use | TradeWise")
public class LearnToUseView extends VerticalLayout {

    private VerticalLayout content;

    public LearnToUseView() {
        addClassName("learn-to-use-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        getStyle().set("background-color", "var(--lumo-base-color)");

        H1 title = new H1("Learn to Use Our Trading Platform");
        title.addClassName("animated-title");

        Paragraph subtitle = new Paragraph("Master the art of trading with our comprehensive guide");
        subtitle.addClassName("animated-subtitle");

        content = new VerticalLayout();
        content.addClassName("content-container");
        content.addClassName("animated-content");
        content.setWidth("100%");
        content.setMaxWidth("1000px");
        content.setPadding(true);

        add(title, subtitle, content);

        // Set default content
        setContent("overview");
    }

    private void setContent(String tabName) {
        content.removeAll();
        content.removeClassName("fade-in");
        content.addClassName("fade-out");

        // Use a small delay to allow the fade-out animation to complete
        UI.getCurrent().access(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            content.removeClassName("fade-out");
            content.addClassName("fade-in");

            switch (tabName.toLowerCase()) {
                case "overview":
                    content.add(createOverviewContent());
                    break;
                case "dashboard":
                    content.add(createFeatureContent("Dashboard", 
                        "Your command center for market insights and portfolio overview.",
                        VaadinIcon.DASHBOARD,
                        new String[]{
                            "View real-time market data and indices",
                            "Check your watchlist for quick stock updates",
                            "See your portfolio summary and performance",
                            "Access quick trade options for your favorite stocks"
                        },
                        "dashboard"));
                    break;
                case "orders":
                    content.add(createFeatureContent("Orders", 
                        "Place and manage your trades with ease.",
                        VaadinIcon.CART,
                        new String[]{
                            "Place market, limit, and stop orders",
                            "View and modify pending orders",
                            "Check order history and execution details",
                            "Set up advanced order types like brackets and covers"
                        },
                        "orders"));
                    break;
                case "holdings":
                    content.add(createFeatureContent("Holdings", 
                        "Track and manage your long-term investments.",
                        VaadinIcon.BRIEFCASE,
                        new String[]{
                            "View all your current stock holdings",
                            "Check individual stock performance",
                            "Analyze your overall portfolio returns",
                            "Easily sell holdings or add to existing positions"
                        },
                        "holdings"));
                    break;
                case "positions":
                    content.add(createFeatureContent("Positions", 
                        "Monitor and manage your intraday trades and F&O positions.",
                        VaadinIcon.TRENDING_UP,
                        new String[]{
                            "Track open positions for stocks and derivatives",
                            "View profit/loss for each position",
                            "Square off or convert positions as needed",
                            "Analyze position-wise and overall P&L"
                        },
                        "positions"));
                    break;
                case "funds":
                    content.add(createFeatureContent("Funds", 
                        "Manage your trading capital efficiently.",
                        VaadinIcon.WALLET,
                        new String[]{
                            "View your available balance and used margin",
                            "Add funds to your account (simulated)",
                            "Withdraw funds from your account (simulated)",
                            "Check detailed ledger for all fund movements"
                        },
                        "funds"));
                    break;
            }
        });
    }

    private VerticalLayout createOverviewContent() {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("overview-content");
        layout.setAlignItems(Alignment.CENTER);

        H2 welcomeTitle = new H2("Welcome to Our Trading Platform");
        welcomeTitle.addClassName("welcome-title");
        Paragraph welcomeDescription = new Paragraph("Learn how to navigate and use our powerful tools");
        welcomeDescription.addClassName("welcome-description");

        HorizontalLayout featuresLayout = new HorizontalLayout();
        featuresLayout.addClassName("features-layout");
        featuresLayout.setWidth("100%");

        String[] features = {"Dashboard", "Orders", "Holdings", "Positions", "Funds"};
        for (String feature : features) {
            Button featureButton = new Button(feature, getIconForFeature(feature));
            featureButton.addClassName("feature-button");
            featureButton.addClickListener(e -> setContent(feature));
            featuresLayout.add(featureButton);
        }

        layout.add(welcomeTitle, welcomeDescription, featuresLayout);
        return layout;
    }

    private VerticalLayout createFeatureContent(String title, String description, VaadinIcon icon, String[] steps, String route) {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("feature-content");
        layout.setAlignItems(Alignment.START);

        H2 featureTitle = new H2(title);
        featureTitle.addClassName("feature-title");
        Icon featureIcon = icon.create();
        featureIcon.addClassName("feature-icon");
        featureTitle.add(featureIcon);

        Paragraph featureDescription = new Paragraph(description);
        featureDescription.addClassName("feature-description");

        H2 howToUseTitle = new H2("How to use:");
        howToUseTitle.addClassName("how-to-use-title");
        VerticalLayout stepsLayout = new VerticalLayout();
        stepsLayout.addClassName("steps-layout");
        for (int i = 0; i < steps.length; i++) {
            Paragraph step = new Paragraph((i + 1) + ". " + steps[i]);
            step.addClassName("step");
            stepsLayout.add(step);
        }

        Button goBackButton = new Button("Go Back", VaadinIcon.ARROW_LEFT.create());
        goBackButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        goBackButton.addClassName("go-back-button");
        goBackButton.addClickListener(e -> setContent("overview"));

        Button tryItNowButton = new Button("Try it now");
        tryItNowButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        tryItNowButton.addClassName("try-it-now-button");
        tryItNowButton.addClickListener(e -> tryItNowButton.getUI().ifPresent(ui -> ui.navigate(route)));

        HorizontalLayout buttonContainer = new HorizontalLayout(goBackButton, tryItNowButton);
        buttonContainer.addClassName("button-container");
        buttonContainer.setWidthFull();
        buttonContainer.setJustifyContentMode(JustifyContentMode.BETWEEN);

        layout.add(featureTitle, featureDescription, howToUseTitle, stepsLayout, buttonContainer);
        return layout;
    }

    private Icon getIconForFeature(String feature) {
        Icon icon;
        switch (feature.toLowerCase()) {
            case "dashboard":
                icon = VaadinIcon.DASHBOARD.create();
                break;
            case "orders":
                icon = VaadinIcon.CART.create();
                break;
            case "holdings":
                icon = VaadinIcon.BRIEFCASE.create();
                break;
            case "positions":
                icon = VaadinIcon.TRENDING_UP.create();
                break;
            case "funds":
                icon = VaadinIcon.WALLET.create();
                break;
            default:
                icon = VaadinIcon.INFO_CIRCLE.create();
        }
        icon.addClassName("feature-icon");
        return icon;
    }
}

