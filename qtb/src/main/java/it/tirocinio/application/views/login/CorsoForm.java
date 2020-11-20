package it.tirocinio.application.views.login;

import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;

import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;

public class CorsoForm extends FormLayout {
	TextField nomeCorso = new TextField("nome Corso");
	Button save = new Button("save");
	Binder<Corso> binder =new Binder<>(Corso.class);
	private CorsoService corsoS;
	
	public CorsoForm(CorsoService c,Utente nomed){
		this.corsoS=c;
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
			corso.setUtentifreq(new ArrayList<Utente>());
			binder.setBean(corso);
			if(binder.validate().isOk())
			this.corsoS.save(corso);
		});
		add(nomeCorso,save);
		
	}
}
