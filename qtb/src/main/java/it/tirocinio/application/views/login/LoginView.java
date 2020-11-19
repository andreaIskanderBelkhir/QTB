package it.tirocinio.application.views.login;

import java.util.Collections;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value="login")
@PageTitle("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver{

	LoginForm login = new LoginForm();
	//Tab registrati = new Tab();
	
	public LoginView(){
		addClassName("login-view");
		setSizeFull();
		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);
		login.setAction("login");
		login.setForgotPasswordButtonVisible(false);
		//registrati.add(new RouterLink("registration",UtenteForm.class));     Per ora  registra solo admin a mano
		//ComponentUtil.setData(registrati, Class.class, UtenteForm.class);
		add(
				new H1("QTB"),
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
