package it.tirocinio.application.view.form;



import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import ch.qos.logback.core.net.LoginAuthenticator;
import it.tirocinio.application.views.login.LoginView;
import it.tirocinio.backend.service.UtentePendingService;
import it.tirocinio.entity.UtentePending;


@Route(value="sign-in")
@PageTitle("registrazione")
@CssImport("./styles/views/hello/hello-view.css")
public class UtenteDaApprovareForm extends FormLayout{

	TextField nome = new TextField("nome");
	TextField email = new TextField("email");
	TextArea descrizione = new TextArea("motivo per registrazione?");
	Button save = new Button();
	Button delete = new Button();
	Button tornab = new Button();
	Binder<UtentePending> binder = new Binder<>(UtentePending.class);
	private UtentePendingService UtPS;



	public UtenteDaApprovareForm(UtentePendingService u) {
		this.UtPS=u;
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		binder.bindInstanceFields(this);
		email.setErrorMessage("Please enter a valid email address");
		nome.setRequiredIndicatorVisible(true);
	    email.setRequiredIndicatorVisible(true);
		descrizione.setRequiredIndicatorVisible(true);
		binder.forField(nome)
		.withValidator(new StringLengthValidator(
				"Please add the nome", 1, null))
		.bind(UtentePending::getNome,UtentePending::setNome);
		binder.forField(email)
		.withValidator(new StringLengthValidator(
				"Please add the email", 1, null))
		.bind(UtentePending::getEmail,UtentePending::setEmail);
		binder.forField(descrizione).bind(UtentePending::getDescizione,UtentePending::setDescizione);
		add(
				nome,
				email,
				descrizione,
				createButtonLayout()
				);


	}


	private Component createButtonLayout() {

		save.setText("save");
		delete.setText("cancella");
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		tornab.setText("torna al login");
		Anchor anchor = new Anchor();
		anchor.setHref("login");
		anchor.add(tornab);
		Dialog dialog = new Dialog();
		dialog.add(new Paragraph("è stata inviata la richiesta."));
		dialog.add(new Paragraph("verra informato tramite email per i dati da accesso"));

		dialog.setWidth("500px");
		dialog.setHeight("150px");
		save.addClickListener(click->{
			UtentePending utente = new UtentePending();
			utente.setNome(nome.getValue().trim());
			utente.setEmail(email.getValue().trim());
			utente.setDescizione(descrizione.getValue().toString());
			utente.setAttivato(false);
			binder.setBean(utente);
			if(binder.validate().isOk()){
				UtPS.save(utente);
				dialog.open();
			}
			else{
				Notification.show("errore manca 1 o piu campi");
			}

		});
		delete.addClickListener(click-> {
			nome.setValue("");
			email.setValue("");
			descrizione.setValue("");
		});
		binder.addStatusChangeListener(evt->save.setEnabled(binder.isValid()));
		return new HorizontalLayout(save,delete,anchor);

	}




}
