package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

public class ProfileMenu extends Dialog {
    
    public ProfileMenu() {
        // Set dialog properties
        setWidth("250px");
        getElement().getStyle().set("padding", "0");
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);

        // Create main layout
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setMargin(false);

        // Profile section
        HorizontalLayout profileSection = createProfileSection();
        mainLayout.add(profileSection);

        // Add separator
        Div separator = new Div();
        separator.setHeight("1px");
        separator.getStyle().set("background-color", "var(--lumo-contrast-10pct)");
        separator.setWidth("100%");
        mainLayout.add(separator);

        // Menu items
        mainLayout.add(createMenuItem("Profile Settings", VaadinIcon.COGS));
        mainLayout.add(createMenuItem("Learn to use", VaadinIcon.BOOK));
        mainLayout.add(createLogoutMenuItem());

        add(mainLayout);
    }

    private HorizontalLayout createProfileSection() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setPadding(true);
        layout.setSpacing(true);

        // Profile image
        StreamResource imageResource = new StreamResource("profile.png",
                () -> getClass().getResourceAsStream("/images/profile.png"));
        Image profileImage = new Image(imageResource, "Profile");
        profileImage.setWidth("32px");
        profileImage.setHeight("32px");
        profileImage.getStyle().set("border-radius", "50%");

        // Profile info
        VerticalLayout profileInfo = new VerticalLayout();
        profileInfo.setPadding(false);
        profileInfo.setSpacing(false);

        Span username = new Span("Username");
        username.getStyle()
               .set("font-weight", "500")
               .set("font-size", "14px");

        Span email = new Span("username@gmail.com");
        email.getStyle()
             .set("color", "var(--lumo-secondary-text-color)")
             .set("font-size", "12px");

        profileInfo.add(username, email);

        layout.add(profileImage, profileInfo);
        return layout;
    }

    private Button createMenuItem(String text, VaadinIcon icon) {
        Button menuItem = new Button(text);
        menuItem.setIcon(new Icon(icon));
        menuItem.getStyle()
                .set("width", "100%")
                .set("justify-content", "flex-start")
                .set("padding", "8px 16px")
                .set("color", "var(--lumo-body-text-color)")
                .set("background-color", "transparent")
                .set("border", "none")
                .set("cursor", "pointer")
                .set("font-size", "14px");

        // Hover effect
        menuItem.addClassName("profile-menu-item");
        getElement().executeJs(
            "this.shadowRoot.querySelector('.profile-menu-item').addEventListener('mouseover', function() {" +
            "  this.style.backgroundColor = 'var(--lumo-contrast-5pct)';" +
            "});" +
            "this.shadowRoot.querySelector('.profile-menu-item').addEventListener('mouseout', function() {" +
            "  this.style.backgroundColor = 'transparent';" +
            "});"
        );

        return menuItem;
    }

    private Button createLogoutMenuItem() {
        Button logoutButton = createMenuItem("Logout", VaadinIcon.SIGN_OUT);
        logoutButton.getStyle().set("color", "var(--lumo-error-color)");
        return logoutButton;
    }
}