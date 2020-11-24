package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;

import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;

public class IscrizioneCorsoForm extends FormLayout {
	ComboBox<Corso> nomeCorso= new ComboBox<>();
	private Utente studente;
	Button save = new Button("isciviti");
	private CorsoService corsoS;
	private UtenteService utenteS;
	
	
	public IscrizioneCorsoForm(CorsoService c,UtenteService u,Utente nomes){
		this.studente=nomes;
		this.utenteS=u;
		this.corsoS=c;
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");	
		PopulateBox();
		save.addClickListener(e->{
			if((nomeCorso.getValue()==null)||(this.corsoS.partecipa(nomeCorso.getValue(),studente))){
				Notification.show("inserire un corso valido");
			}
			else{
			corsoS.addStudente(studente, nomeCorso.getValue());
			utenteS.AddCorso(nomeCorso.getValue(), studente);
			Notification.show("ti sei iscritto");
			}
		});
		add(nomeCorso,save);
		
	}


	private void PopulateBox() {
		nomeCorso.setLabel("scegli un corso");
		List<Corso> corsi= this.corsoS.findAll();
		List<Corso> possibili= new ArrayList<Corso>();
		for(Corso c: corsi){		
				if(!(this.corsoS.partecipa(c,studente))){
					possibili.add(c);
				}
	
		}
		nomeCorso.setItemLabelGenerator(Corso::getNomeCorso);
		nomeCorso.setItems(possibili);	
		
	}
}
