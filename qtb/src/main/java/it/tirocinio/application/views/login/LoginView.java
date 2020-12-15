package it.tirocinio.application.views.login;

import java.util.Collections;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.ui.Label;

import it.tirocinio.application.view.form.UtenteDaApprovareForm;
import it.tirocinio.backend.service.UtentePendingService;


@Route(value="login")
@PageTitle("login")

public class LoginView extends HorizontalLayout implements BeforeEnterObserver{

	LoginOverlay login = new LoginOverlay();
	private UtentePendingService utenteDaApprovareService;
	
	//Tab registrati = new Tab();
	
	public LoginView(UtentePendingService utenteService){
		this.utenteDaApprovareService=utenteService;
		UtenteDaApprovareForm registrazioneForm= new UtenteDaApprovareForm(utenteService);
		setSizeFull();
		H1 logintitle = new H1("QTB");
		Image titleComponent = new Image("images/missing.jpg", "img");
		titleComponent.getStyle().set("padding", "5px");
		titleComponent.getStyle().set("width", "300px");
		titleComponent.getStyle().set("height", "250px");
		titleComponent.getStyle().set("margin-top", "50px");
		login.setTitle(titleComponent);
		/*
		logintitle.getStyle().set("padding-left","110px");
		logintitle.getStyle().set("font-style","italic");
		login.setTitle(logintitle);
		*/
		LoginI18n loginForm = LoginI18n.createDefault();
		loginForm.getForm().setForgotPassword("Registrati");
		login.setI18n(loginForm);	
		login.setAction("login");     
		login.setOpened(true);
		login.setDescription("Effetua il Log-In con le credenziali fornite");
		login.setForgotPasswordButtonVisible(true);
		login.addForgotPasswordListener(e->{
			login.close();
			add(registrazioneForm);
		});
		login.addLoginListener(e->login.close());
		add(
				login
				);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if(!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty()){
			login.setError(true);
		}
		
	}
}
