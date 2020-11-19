package it.tirocinio.application.views.login;



import org.openqa.selenium.Alert;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;


@Route(value = "registration", layout = MainView.class)
@PageTitle("reg")
@CssImport("./styles/views/hello/hello-view.css")
public class UtenteForm extends FormLayout{

	TextField nome = new TextField();
	PasswordField password = new PasswordField();
	ComboBox<String> ruolo = new ComboBox<>();
	Button save = new Button();
	Button delete = new Button();
	Binder<Utente> binder = new Binder<>(Utente.class);
	private UtenteService utenteService;



	public UtenteForm(UtenteService utenteService) {
		this.utenteService=utenteService;
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		binder.bindInstanceFields(this);
		nome.setLabel("nome");
		password.setLabel("password");
		ruolo.setLabel("ruolo");
		ruolo.setItems("professore","studente");
		nome.setRequiredIndicatorVisible(true);
		password.setRequiredIndicatorVisible(true);
		ruolo.setRequiredIndicatorVisible(true);
		binder.forField(nome)
		.withValidator(new StringLengthValidator(
				"Please add the nome", 1, null))
		.bind(Utente::getNome,Utente::setNome);
		binder.forField(password)
		.withValidator(new StringLengthValidator(
				"Please add the password", 1, null))
		.bind(Utente::getPassword,Utente::setPassword);
		binder.forField(ruolo).bind(Utente::getRuolo,Utente::setRuolo);
		binder
		.withValidator((item,valueContex) ->{
			if(item.getRuolo().equals("professore") || item.getRuolo().equals("studente")){
				return ValidationResult.ok();
			}	
			return ValidationResult.error("inserire un ruolo valido");
				
		});


		add(
				nome,
				password,
				ruolo,
				createButtonLayout()
				);


	}


	private Component createButtonLayout() {

		save.setText("save");
		delete.setText("cancella");
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		save.addClickListener(click->{
			Utente utente = new Utente();
			utente.setNome(nome.getValue());
			utente.setPassword(password.getValue());
			utente.setRuolo(ruolo.getValue());
			binder.setBean(utente);
			if(binder.validate().isOk()){
				utenteService.save(utente);
				Notification.show("Salvato");
			}
			else{
				Notification.show("errore manca 1 o piu campi");
			}

		});
		delete.addClickListener(click-> {
			nome.setValue("");
			password.setValue("");
			ruolo.setValue("");
		});
		binder.addStatusChangeListener(evt->save.setEnabled(binder.isValid()));
		return new HorizontalLayout(save,delete);

	}




}
