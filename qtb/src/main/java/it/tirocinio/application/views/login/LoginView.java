package it.tirocinio.application.views.login;

import java.util.Collections;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.view.form.UtenteDaApprovareForm;
import it.tirocinio.backend.UtenteRepository;
import it.tirocinio.backend.service.UtentePendingService;
import it.tirocinio.backend.service.UtenteService;


@Route(value="login")
@PageTitle("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver{

	LoginOverlay login = new LoginOverlay();
	private UtentePendingService utenteDaApprovareService;
	UtenteDaApprovareForm registrazioneForm;
	//Tab registrati = new Tab();
	
	public LoginView(UtentePendingService utenteService){
		this.utenteDaApprovareService=utenteService;
		addClassName("login-view");
		setSizeFull();
		login.setAction("login");
		H1 logintitle = new H1("QTB");
		logintitle .getStyle().set("padding-left","110px");
		logintitle .getStyle().set("font-style","italic");
		login.setTitle(logintitle);
		login.setDescription("Effetua il Log-In con le credenziali fornite");
		login.setForgotPasswordButtonVisible(false);
		login.addLoginListener(e->login.close());
		
		registrazioneForm= new UtenteDaApprovareForm(this.utenteDaApprovareService);
		registrazioneForm.setVisible(false);
		Button open = new Button("Clicca per fare il Log-in",e->login.setOpened(true));
		H1 title = new H1("QTB");
		H3 testo =new H3("Benvenuto in questa web app ");
		H3 testo2 =new H3("clicca sul pulsante per fare il Log-in oppure sul pulsante di registrazione per inviare i tuoi dati ");
		Button reg = new Button("Registrazione",e->registrazioneForm.setVisible(true));
		HorizontalLayout pulsanti = new HorizontalLayout();
		pulsanti.setAlignItems(Alignment.CENTER);
		pulsanti.add(open,reg);
		
		//registrati.add(new RouterLink("registration",UtenteForm.class));     Per ora  registra solo admin a mano
		//ComponentUtil.setData(registrati, Class.class, UtenteForm.class);
		add(
				title,		
				testo,
				testo2,
				login,
				pulsanti,
				registrazioneForm
				);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if(!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty()){
			login.setError(true);
		}
		
	}
}
