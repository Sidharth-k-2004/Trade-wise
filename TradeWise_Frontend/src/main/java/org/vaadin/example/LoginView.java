package org.vaadin.example;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

@Route("login")
@PageTitle("Login | TradeWise")
public class LoginView extends VerticalLayout {

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setSpacing(false);
        setPadding(false);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        HorizontalLayout card = createLoginCard();
        add(card);

        getStyle().set("background-color", "#f5f5f5");
    }

    private HorizontalLayout createLoginCard() {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("login-card");
        card.setSpacing(false);

        VerticalLayout leftSection = createLeftSection();
        VerticalLayout rightSection = createRightSection();

        card.add(leftSection, rightSection);
        
        // Style the card
        card.getStyle()
            .set("background-color", "white")
            .set("border-radius", "12px")
            .set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.1)");
        
        return card;
    }

    private VerticalLayout createLeftSection() {
        VerticalLayout leftSection = new VerticalLayout();
        leftSection.addClassName("login-left-section");
        
        H1 welcomeText = new H1("Welcome Back!");
        welcomeText.getStyle()
            .set("color", "#0066FF")
            .set("margin", "0")
            .set("font-size", "2.5em");
        
        Div subtitle = new Div();
        subtitle.setText("Login to your account");
        subtitle.getStyle()
            .set("color", "#666")
            .set("margin-top", "1em");
        
        Div platformText = new Div();
        platformText.setText("Your Trading Platform");
        platformText.getStyle()
            .set("color", "#333")
            .set("margin-top", "2em")
            .set("font-weight", "500");
        
        leftSection.add(welcomeText, subtitle, platformText);
        leftSection.setAlignItems(Alignment.CENTER);
        leftSection.setJustifyContentMode(JustifyContentMode.CENTER);
        leftSection.setSpacing(false);
        leftSection.setPadding(true);
        leftSection.setWidth("300px");
        
        return leftSection;
    }

    private VerticalLayout createRightSection() {
        VerticalLayout rightSection = new VerticalLayout();
        rightSection.addClassName("login-right-section");
        
        StreamResource logoResource = new StreamResource("logo.png",
               () -> getClass().getResourceAsStream("/images/logo.png"));
        Image logoIcon = new Image(logoResource, "TradeWise");
        logoIcon.setHeight("60px");
        logoIcon.addClassName("login-logo");
        
        H1 loginTitle = new H1("Login");
        loginTitle.getStyle()
            .set("color", "#333")
            .set("margin", "1em 0")
            .set("font-size", "2em");

        EmailField emailField = new EmailField();
        emailField.setPlaceholder("Email");
        emailField.setPrefixComponent(new Icon(VaadinIcon.ENVELOPE));
        emailField.setWidth("100%");
        emailField.getStyle()
            .set("margin-bottom", "1em")
            .set("--lumo-contrast-10pct", "#E8EFF5");

        PasswordField passwordField = new PasswordField();
        passwordField.setPlaceholder("Password");
        passwordField.setPrefixComponent(new Icon(VaadinIcon.LOCK));
        passwordField.setWidth("100%");
        passwordField.getStyle()
            .set("margin-bottom", "2em")
            .set("--lumo-contrast-10pct", "#E8EFF5");

        Button loginButton = new Button("Login");
        loginButton.addClassName("login-button");
        loginButton.getStyle()
            .set("background-color", "#0066FF")
            .set("color", "white")
            .set("border-radius", "4px")
            .set("width", "100%")
            .set("padding", "1em");
        
        // loginButton.addClickListener(e -> {
        //     String email = emailField.getValue();
        //     String password = passwordField.getValue();

        //     if (isValidInput(email, password)) {
        //         Map<String, String> userMap = new HashMap<>();
        //         userMap.put("email", email);
        //         userMap.put("password", password);

        //         RestTemplate restTemplate = new RestTemplate();
        //         try {
        //             String url = "http://localhost:8080/authenticate";
        //             Boolean isAuthenticated = restTemplate.postForObject(url, userMap, Boolean.class);
        //             if (isAuthenticated != null && isAuthenticated) {
        //                 showNotification("Login successful!", "success-notification");
        //                 loginButton.getUI().ifPresent(ui -> ui.navigate("dashboard"));
        //             } else {
        //                 showNotification("Invalid email or password", "error-notification");
        //             }
        //         } catch (Exception ex) {
        //             showNotification("Login failed. Try again.", "error-notification");
        //         }
        //     } else {
        //         showNotification("Please enter a valid email and password", "error-notification");
        //     }
        // });
        loginButton.addClickListener(e -> {
            String email = emailField.getValue();
            String password = passwordField.getValue();

            if (isValidInput(email, password)) {
                Map<String, String> userMap = new HashMap<>();
                userMap.put("email", email);
                userMap.put("password", password);

                RestTemplate restTemplate = new RestTemplate();
                try {
                    String url = "http://localhost:8080/authenticate";
                    ResponseEntity<Map> response = restTemplate.postForEntity(url, userMap, Map.class);
                    System.out.println(response);
                    Map<String, Object> responseBody = response.getBody();

                    if (responseBody != null && responseBody.containsKey("userId") && responseBody.get("status").equals("success")) {
                        // Store userId in VaadinSession
                        VaadinSession.getCurrent().setAttribute("userId", responseBody.get("userId"));

                        showNotification("Login successful!", "success-notification");
                        loginButton.getUI().ifPresent(ui -> ui.navigate("dashboard"));
                    } else {
                        showNotification("Invalid email or password", "error-notification");
                    }
                } catch (Exception ex) {
                    showNotification("Login failed. Try again.", "error-notification");
                }
            } else {
                showNotification("Please enter a valid email and password", "error-notification");
            }
        });


        Div signupPrompt = new Div();
        signupPrompt.getStyle().set("margin-top", "2em");
        signupPrompt.setText("Don't have an account? ");
        Anchor signupLink = new Anchor("signup", "Sign up");
        signupLink.getStyle()
            .set("color", "#0066FF")
            .set("text-decoration", "none")
            .set("font-weight", "500");
        signupPrompt.add(signupLink);

        rightSection.add(logoIcon, loginTitle, emailField, passwordField, loginButton, signupPrompt);
        rightSection.setAlignItems(Alignment.CENTER);
        rightSection.setJustifyContentMode(JustifyContentMode.CENTER);
        rightSection.setPadding(true);
        rightSection.setSpacing(false);
        rightSection.setWidth("300px");
        
        return rightSection;
    }

    private boolean isValidInput(String email, String password) {
        return email != null && !email.isEmpty() && email.contains("@") &&
               password != null && !password.isEmpty();
    }

    private void showNotification(String message, String className) {
        Notification notification = new Notification(message, 3000, Notification.Position.TOP_CENTER);
        notification.getElement().getThemeList().add(className);
        notification.open();
    }
}

