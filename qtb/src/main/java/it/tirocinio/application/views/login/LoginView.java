package it.tirocinio.application.views.login;

import java.util.Collections;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import it.tirocinio.application.view.form.UtenteDaApprovareForm;
import it.tirocinio.backend.service.UtentePendingService;


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
		H1 logintitle = new H1("QTB");
		logintitle.getStyle().set("padding-left","110px");
		logintitle.getStyle().set("font-style","italic");
		login.setTitle(logintitle);
		LoginI18n loginForm = LoginI18n.createDefault();
		loginForm.getForm().setForgotPassword("Registrati");
		login.setI18n(loginForm);	
		login.setAction("login");
       
		login.setOpened(true);
		login.setTitle(logintitle);
		login.setDescription("Effetua il Log-In con le credenziali fornite");
		login.setForgotPasswordButtonVisible(true);
		login.addForgotPasswordListener(e->{
			login.close();
			add(new RouterLink("",UtenteDaApprovareForm.class));
		});
		login.addLoginListener(e->login.close());
		
		
		
		//registrati.add(new RouterLink("registration",UtenteForm.class));     Per ora  registra solo admin a mano
		//ComponentUtil.setData(registrati, Class.class, UtenteForm.class);
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
