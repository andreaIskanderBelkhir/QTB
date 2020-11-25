package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;

import it.tirocinio.application.views.hello.ProfessoreView;
import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;

public class CorsoForm extends FormLayout {
	TextField nomeCorso = new TextField("nome Corso");
	Button save = new Button("save");
	Binder<Corso> binder =new Binder<>(Corso.class);
	private CorsoService corsoS;
	private UtenteService utenteS;
	
	public CorsoForm(CorsoService c,UtenteService u,Utente nomed){
		this.corsoS=c;
		this.utenteS=u;
		Notification notification = new Notification(
		        "è stato aggiunto ti prego di aggiornare la pagina ", 3000,
		        Position.TOP_CENTER);
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		binder.bindInstanceFields(this);
		binder.forField(nomeCorso).withValidator(new StringLengthValidator(
				"Please add the nome", 1, null)).bind(Corso::getNomeCorso,Corso::setNomeCorso);
		save.addClickListener(e->{
			Corso corso=new Corso();
			corso.setNomeCorso(nomeCorso.getValue());
			corso.setDocente(nomed);	
			binder.setBean(corso);
			if((binder.validate().isOk()) && this.corsoS.corsoNonEsistente(corso)){	
			this.corsoS.save(corso);
			this.utenteS.AddCorso(corso,nomed);	
			notification.open();
			
			}
			else
				Notification.show("error inserire un corso valido");
		});
		add(nomeCorso,save);
		
	}
}


