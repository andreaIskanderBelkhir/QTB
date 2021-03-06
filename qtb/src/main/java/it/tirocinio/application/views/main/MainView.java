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
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
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
import it.tirocinio.application.views.hello.Domande2view;
import it.tirocinio.application.views.hello.DomandeView;
import it.tirocinio.application.views.hello.IscrizioniView;
import it.tirocinio.application.views.hello.ProfessoreView;
import it.tirocinio.application.views.hello.QuizView;
import it.tirocinio.application.views.hello.SelezioneView;
import it.tirocinio.application.views.hello.StudenteView;
import it.tirocinio.application.views.hello.VisualizzaPassatiView;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@CssImport("./styles/views/main/main-view.css")
@PWA(name = "Tester", shortName = "Tester", enableInstallPrompt = false)
public class MainView extends AppLayout {

	private final Tabs menu;
	private H1 viewTitle;
	boolean hasUserRole=false;
	boolean hasadminRole=false;
	boolean hasProfRole=false;
	boolean hasCandidatoRole=false;
	private String nome;

	public MainView() {
		setPrimarySection(Section.DRAWER);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		this.hasUserRole = authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
		this.hasadminRole = authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		this.hasProfRole = authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_PROFESSORE"));
		this.hasCandidatoRole = authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_CANDIDATO"));

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ( principal instanceof UserDetails){
			this.nome = ((UserDetails)principal).getUsername();
		}


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
		Anchor logout = new Anchor("/logout");
		Button buttonLogout =new Button("logout",new Icon(VaadinIcon.CLOSE));
		buttonLogout.setIconAfterText(true);
		logout.add(buttonLogout);
		layout.expand(viewTitle);
		Div div1 = new Div();      
		div1.add("Username : ");
		div1.add(this.nome + "  ");
		layout.add(div1);
		layout.add(new Image("images/user.png", "Avatar"));

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
		VerticalLayout logoLayout = new VerticalLayout();
		logoLayout.setId("logo");
		logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
		logoLayout.add(new Image("images/logo_tester.png", "Tester logo"));
		
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
				else
					if(this.hasCandidatoRole)
						tabs.add(createMenuItemsCandidato());
		return tabs;
	}

	private Component[] createMenuItemsUser() {
		return new Tab[] {
				createTab("Homepage", HomePageView.class),
				createTab("I miei corsi",StudenteView.class),
		};
	}
	private Component[] createMenuItemsCandidato() {
		return new Tab[] {
				createTab("Homepage", HomePageView.class),
				createTab("Selezione",SelezioneView.class),
				
		};
	}	
	private Component[] createMenuItemsProf() {
		return new Tab[] {
				createTab("Homepage", HomePageView.class),
				createTab("Gestione Corsi",ProfessoreView.class),
				createTab("Gestione Test",QuizView.class),
				//createTab("Gestione Domande",DomandeView.class),
				createTab("Gestione Domande ",Domande2view.class),
				createTab("Gestione Iscrizioni",IscrizioniView.class),
				createTab("Gestione Selezione",SelezioneView.class),
				createTab("Visualizza Passati",VisualizzaPassatiView.class)
		};
	}

	private Component[] createMenuItemsAdmin() {
		return new Tab[] {
				createTab("Homepage", HomePageView.class),
				createTab("I miei corsi",StudenteView.class),
				createTab("Gestione Corsi",ProfessoreView.class),
				createTab("Gestione Test",QuizView.class),
				//createTab("Gestione Domande",DomandeView.class),
				createTab("Gestione Domande ",Domande2view.class),
				createTab("Gestione Iscrizioni",IscrizioniView.class),
				createTab("Visualizza Passati",VisualizzaPassatiView.class),
				createTab("Gestione Selezione",SelezioneView.class),
				createTab("Admin",AdminView.class),
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
