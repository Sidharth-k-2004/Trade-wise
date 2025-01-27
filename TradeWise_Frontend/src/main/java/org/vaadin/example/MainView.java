// package org.vaadin.example;


// import com.vaadin.flow.component.button.Button;
// import com.vaadin.flow.component.html.Div;
// import com.vaadin.flow.component.html.H1;
// import com.vaadin.flow.component.html.H2;
// import com.vaadin.flow.component.html.H3;
// import com.vaadin.flow.component.html.Image;
// import com.vaadin.flow.component.html.Paragraph;
// import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
// import com.vaadin.flow.component.orderedlayout.VerticalLayout;
// import com.vaadin.flow.router.PageTitle;
// import com.vaadin.flow.router.Route;
// import com.vaadin.flow.server.StreamResource;

// @Route("")
// @PageTitle("TradeWise")
// public class MainView extends VerticalLayout {

//     public MainView() {
//         addClassName("main-view");
//         setSpacing(false);
//         setPadding(false);
//         setSizeFull();

//         // Header
//         HorizontalLayout header = new HorizontalLayout();
//         header.addClassName("header");
        
//         HorizontalLayout logoSection = new HorizontalLayout();
//         Image logoIcon = new Image(new StreamResource("logo.png", 
//             () -> getClass().getResourceAsStream("/images/logo.png")), "TradeWise");
//         logoIcon.setHeight("180px");
//         logoSection.add(logoIcon);
        
//         HorizontalLayout navButtons = new HorizontalLayout();
//         Button signupBtn = new Button("Signup");
//         Button knowMoreBtn = new Button("Know More");
//         navButtons.add(signupBtn, knowMoreBtn);
        
//         header.add(logoSection, navButtons);
//         header.setJustifyContentMode(JustifyContentMode.BETWEEN);
//         header.setWidthFull();
//         header.setPadding(true);

//         // Hero Section
//         VerticalLayout heroSection = new VerticalLayout();
//         heroSection.addClassName("hero-section");
        
//         Image heroImage = new Image(new StreamResource("investment-illustration.png", 
//             () -> getClass().getResourceAsStream("/images/investment-illustration.png")), 
//             "Investment Illustration");
//         heroImage.setWidth("600px");
        
//         H1 mainTitle = new H1("Invest in everything");
//         Paragraph subtitle = new Paragraph("Your platform to explore and understand the world of investments.");
//         Button signupHeroBtn = new Button("Sign up for free");
//         signupHeroBtn.addClassName("primary-button");
        
//         heroSection.add(heroImage, mainTitle, subtitle, signupHeroBtn);
//         heroSection.setAlignItems(Alignment.CENTER);
//         heroSection.setHorizontalComponentAlignment(Alignment.CENTER);

//         // Features Section
//         VerticalLayout featuresSection = new VerticalLayout();
//         featuresSection.addClassName("features-section");
        
//         H2 featuresTitle = new H2("Simplifying Investments, Empowering Your Financial Growth");
        
//         // First Feature Row (Text Left, Image Right)
//         HorizontalLayout feature1 = new HorizontalLayout();
//         VerticalLayout feature1Text = new VerticalLayout();
//         feature1Text.add(
//             new H3("Invest with confidence"),
//             new Paragraph("Committed to providing a seamless and secure, investment experience tailored to your needs."),
//             new H3("Investment Made Simple"),
//             new Paragraph("Easy-to-use tools to help you invest smarter and achieve your goals."),
//             new H3("A New Way to Invest"),
//             new Paragraph("Reimagining investment platforms—focused on your experience and growth.")
//         );
        
//         Image feature1Image = new Image(new StreamResource("investment-growth.png", 
//             () -> getClass().getResourceAsStream("/images/investment-growth.png")), 
//             "Investment Growth");
//         feature1Image.setWidth("400px");
        
//         feature1.add(feature1Text, feature1Image);
//         feature1.setWidthFull();

//         // CTA Section
//         VerticalLayout ctaSection = new VerticalLayout();
//         ctaSection.addClassName("cta-section");
        
//         Image ctaImage = new Image(new StreamResource("financial-control.png", 
//             () -> getClass().getResourceAsStream("/images/financial-control.png")), 
//             "Financial Control");
//         ctaImage.setWidth("200px");
        
//         H2 ctaTitle = new H2("Take Control of Your Finances");
//         Paragraph ctaText = new Paragraph("Empowering you with the right tools to make confident investment decisions.");
        
//         H3 simplicityTitle = new H3("Simplicity at its best");
//         Paragraph simplicityText = new Paragraph("Focus on your goals without distractions—no gimmicks, just reliable tools and features.");
        
//         H2 accountTitle = new H2("Open an account");
//         Paragraph accountText = new Paragraph("Modern platform, 0 investments");
//         Button signupCtaBtn = new Button("Sign up for free");
//         signupCtaBtn.addClassName("primary-button");
        
//         ctaSection.add(ctaImage, ctaTitle, ctaText, simplicityTitle, simplicityText, 
//                       accountTitle, accountText, signupCtaBtn);
//         ctaSection.setAlignItems(Alignment.CENTER);

//         add(header, heroSection, featuresSection, featuresTitle, feature1, ctaSection);
//     }
// }





package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route("") // This maps to root URL
@PageTitle("TradeWise")
public class MainView extends VerticalLayout {

    public MainView() {
        addClassName("main-view");
        setSpacing(true);
        setPadding(false);
        setSizeFull();

        // Header
        HorizontalLayout header = createHeader();

        // Hero Section
        VerticalLayout heroSection = createHeroSection();

        // Features Section
        VerticalLayout featuresSection = createFeaturesSection();

        // CTA Section
        VerticalLayout ctaSection = createCTASection();

        // Add all sections to the main view
        add(header, heroSection, featuresSection, ctaSection);
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");

        // Logo Section
        HorizontalLayout logoSection = new HorizontalLayout();
        StreamResource logoResource = new StreamResource("logo.png",
                () -> getClass().getResourceAsStream("/images/logo.png"));
        Image logoIcon = new Image(logoResource, "TradeWise");
        logoIcon.setHeight("90px");
        logoSection.add(logoIcon);

        // Navigation Buttons
        HorizontalLayout navButtons = new HorizontalLayout();
        Button signupBtn = new Button("Sign up");
        signupBtn.addClassName("secondary-button");
        signupBtn.addClickListener(e -> signupBtn.getUI().ifPresent(ui -> 
        ui.navigate("signup")));
        Button knowMoreBtn = new Button("Know More");
        knowMoreBtn.addClassName("secondary-button");
        navButtons.add(signupBtn, knowMoreBtn);

        header.add(logoSection, navButtons);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setWidthFull();
        header.setPadding(true);
        header.setAlignItems(Alignment.CENTER);

        return header;
    }

    private VerticalLayout createHeroSection() {
        VerticalLayout heroSection = new VerticalLayout();
        heroSection.addClassName("hero-section");

        StreamResource heroResource = new StreamResource("investment-illustration.png",
                () -> getClass().getResourceAsStream("/images/investment-illustration.png"));
        Image heroImage = new Image(heroResource, "Investment Illustration");
        heroImage.setWidth("700px");

        H1 mainTitle = new H1("Invest in everything");
        mainTitle.addClassName("hero-title");
        Paragraph subtitle = new Paragraph("Your platform to explore and understand the world of investments.");
        subtitle.addClassName("hero-subtitle");
        Button signupHeroBtn = new Button("Sign up for free");
        signupHeroBtn.addClassName("primary-button");
        signupHeroBtn.addClickListener(e -> signupHeroBtn.getUI().ifPresent(ui -> 
        ui.navigate("signup")));

        heroSection.add(mainTitle, subtitle, heroImage, signupHeroBtn);
        heroSection.setAlignItems(Alignment.CENTER);

        return heroSection;
    }

    private VerticalLayout createFeaturesSection() {
        VerticalLayout featuresSection = new VerticalLayout();
        featuresSection.addClassName("features-section");

        // Title for the section
        H2 featuresTitle = new H2("Simplifying Investments, Empowering Your Financial Growth");
        featuresTitle.addClassName("section-title");
        featuresSection.add(featuresTitle);

        // Content layout
        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.addClassName("content-layout");
        contentLayout.setWidthFull();
        contentLayout.setSpacing(true);
        contentLayout.setPadding(true);
        contentLayout.setAlignItems(Alignment.CENTER);

        // Text content
        VerticalLayout textContent = new VerticalLayout();
        textContent.setSpacing(true);
        textContent.setPadding(false);

        H3 feature1Title = new H3("Invest with confidence");
        Paragraph feature1Description = new Paragraph("Committed to providing a seamless and secure investment experience tailored to your needs.");

        H3 feature2Title = new H3("Investment Made Simple");
        Paragraph feature2Description = new Paragraph("Easy-to-use tools to help you invest smarter and achieve your goals.");

        H3 feature3Title = new H3("A New Way to Invest");
        Paragraph feature3Description = new Paragraph("Reimagining investment platforms—focused on your experience and growth.");

        textContent.add(feature1Title, feature1Description, feature2Title, feature2Description, feature3Title, feature3Description);

        // Image content
        StreamResource featureImageResource = new StreamResource("investment-illustration.png",
                () -> getClass().getResourceAsStream("/images/investment-illustration.png"));
        Image featureImage = new Image(featureImageResource, "Investment Illustration");
        featureImage.setWidth("500px");
        featureImage.addClassName("feature-image");

        contentLayout.add(textContent, featureImage);
        contentLayout.setFlexGrow(1, textContent);
        contentLayout.setFlexGrow(0, featureImage);

        featuresSection.add(contentLayout);

        return featuresSection;
    }

    private VerticalLayout createCTASection() {
        VerticalLayout ctaSection = new VerticalLayout();
        ctaSection.addClassName("cta-section");

        StreamResource ctaResource = new StreamResource("financial-control.png",
                () -> getClass().getResourceAsStream("/images/financial-control.png"));
        Image ctaImage = new Image(ctaResource, "Financial Control");
        ctaImage.setWidth("200px");

        H2 ctaTitle = new H2("Take Control of Your Finances");
        ctaTitle.addClassName("section-title");
        Paragraph ctaText = new Paragraph("Empowering you with the right tools to make confident investment decisions.");

        H3 simplicityTitle = new H3("Simplicity at its best");
        Paragraph simplicityText = new Paragraph("Focus on your goals without distractions—no gimmicks, just reliable tools and features.");

        H2 accountTitle = new H2("Open an account");
        Paragraph accountText = new Paragraph("Modern platform, 0 investments");
        Button signupCtaBtn = new Button("Sign up for free");
        signupCtaBtn.addClassName("primary-button");
        signupCtaBtn.addClickListener(e -> signupCtaBtn.getUI().ifPresent(ui -> 
        ui.navigate("signup")));

        ctaSection.add(ctaImage, ctaTitle, ctaText, simplicityTitle, simplicityText, accountTitle, accountText, signupCtaBtn);
        ctaSection.setAlignItems(Alignment.CENTER);

        return ctaSection;
    }
}






