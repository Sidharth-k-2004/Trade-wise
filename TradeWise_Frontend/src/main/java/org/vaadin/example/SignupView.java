package org.vaadin.example;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.client.RestTemplate;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
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
import com.vaadin.flow.component.html.Anchor;

@Route("signup")
@PageTitle("Sign Up | TradeWise")
public class SignupView extends VerticalLayout {

    public SignupView() {
        addClassName("signup-view");
        setSizeFull();
        setSpacing(false);
        setPadding(false);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        HorizontalLayout card = createSignupCard();
        add(card);
    }

    private HorizontalLayout createSignupCard() {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("signup-card");
        card.setSpacing(false);

        VerticalLayout leftSection = createLeftSection();
        VerticalLayout rightSection = createRightSection();

        card.add(leftSection, rightSection);
        return card;
    }

    private VerticalLayout createLeftSection() {
        VerticalLayout leftSection = new VerticalLayout();
        leftSection.addClassName("signup-left-section");
        
        H2 welcomeText = new H2("Hello!!!");
        welcomeText.addClassName("welcome-text");
        
        Div subtitle = new Div();
        subtitle.setText("Enter your personal details");
        subtitle.addClassName("welcome-subtitle");
        
        Div subtitle2 = new Div();
        subtitle2.setText("and start with us");
        subtitle2.addClassName("welcome-subtitle");
        
        Div platformText = new Div();
        platformText.setText("Your Trading Platform");
        platformText.addClassName("platform-text");
        
        leftSection.add(welcomeText, subtitle, subtitle2, platformText);
        leftSection.setAlignItems(Alignment.CENTER);
        leftSection.setJustifyContentMode(JustifyContentMode.CENTER);

        return leftSection;
    }

    private VerticalLayout createRightSection() {
        VerticalLayout rightSection = new VerticalLayout();
        rightSection.addClassName("signup-right-section");
        
        StreamResource logoResource = new StreamResource("logo.png",
               () -> getClass().getResourceAsStream("/images/logo.png"));
        Image logoIcon = new Image(logoResource, "TradeWise");
        logoIcon.setHeight("60px");
        logoIcon.addClassName("signup-logo");

        H2 formTitle = new H2("Create Account");
        formTitle.addClassName("form-title");

        EmailField emailField = new EmailField();
        emailField.setPlaceholder("Email");
        emailField.setPrefixComponent(new Icon(VaadinIcon.ENVELOPE));
        emailField.addClassName("signup-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPlaceholder("Password");
        passwordField.setPrefixComponent(new Icon(VaadinIcon.LOCK));
        passwordField.addClassName("signup-field");

        Button signupButton = new Button("Signup");
        signupButton.addClassName("signup-button");
        signupButton.addClickListener(e -> {
    String email = emailField.getValue();
    String password = passwordField.getValue();
        
        

    if (isValidInput(email, password)) {
        // Create a user object or map
        Map<String, String> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("password", password);

        // Use RestTemplate to send POST request
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = "http://localhost:8080/user"; // Replace with your backend URL
            restTemplate.postForObject(url, userMap, String.class);
            showNotification("Signup successful!", "success-notification");
            signupButton.getUI().ifPresent(ui -> ui.navigate("login"));
        } catch (Exception ex) {
            ex.printStackTrace();
            showNotification("Signup failed. Try again.", "error-notification");
        }
    } else {
        showNotification("Please enter a valid email and password", "error-notification");
    }
});
        Div loginPrompt = new Div();
        loginPrompt.setText("Already have an account? ");
        Anchor loginLink = new Anchor("login", "Login");
        loginLink.addClassName("login-link");
        loginPrompt.add(loginLink);
        loginPrompt.addClassName("login-prompt");

        rightSection.add(logoIcon, formTitle, emailField, passwordField, signupButton, loginPrompt);
        rightSection.setAlignItems(Alignment.CENTER);
        rightSection.setJustifyContentMode(JustifyContentMode.CENTER);

        return rightSection;
        
    }

    private boolean isValidInput(String email, String password) {
        return email != null && !email.isEmpty() && email.contains("@") &&
               password != null && password.length() >= 6;
    }

    private void showNotification(String message, String className) {
        Notification notification = new Notification(message, 3000, Notification.Position.TOP_CENTER);
        notification.getElement().getThemeList().add(className);
        notification.open();
    }
}
