package it.tirocinio.application.views.main;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import it.tirocinio.application.views.main.MainView;
import it.tirocinio.application.view.HomePageView;
import it.tirocinio.application.views.hello.AdminView;
import it.tirocinio.application.views.hello.ProfessoreView;
import it.tirocinio.application.views.hello.StudenteView;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@CssImport("./styles/views/main/main-view.css")
@PWA(name = "QTB", shortName = "QTB", enableInstallPrompt = false)
public class MainView extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;
    boolean hasUserRole=false;
    boolean hasadminRole=false;
    boolean hasProfRole=false;

    public MainView() {
        setPrimarySection(Section.DRAWER);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.hasUserRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
        this.hasadminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        this.hasProfRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_PROFESSORE"));
        
        addToNavbar(true, createHeaderContent());
        
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.add(viewTitle);
        Anchor logout = new Anchor("/logout","logout  ");
        layout.expand(viewTitle);
        
        layout.add(new Image("images/user.svg", "Avatar"));
        layout.add(logout);
        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "QTB logo"));
        logoLayout.add(new H1("QTB"));
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        if(this.hasUserRole)
        tabs.add(createMenuItemsUser());
        else
        	if(this.hasProfRole)
        		tabs.add(createMenuItemsProf());
        	else
        		if(this.hasadminRole)
        			tabs.add(createMenuItemsAdmin());
        return tabs;
    }

    private Component[] createMenuItemsUser() {
        return new Tab[] {
        	createTab("Homepage", HomePageView.class),
        	createTab("studente",StudenteView.class)
        };
    }
    private Component[] createMenuItemsProf() {
        return new Tab[] {
        	createTab("Homepage", HomePageView.class),
        	createTab("professore",ProfessoreView.class)
        };
    }
    private Component[] createMenuItemsAdmin() {
        return new Tab[] {
        	createTab("Homepage", HomePageView.class),
        	createTab("studente",StudenteView.class),
        	createTab("professore",ProfessoreView.class),
        	createTab("admin",AdminView.class),
        };
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren()
                .filter(tab -> ComponentUtil.getData(tab, Class.class)
                        .equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}
