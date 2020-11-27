package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;

import it.tirocinio.application.views.hello.ProfessoreView;
import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Quiz;

public class CorsoForm extends FormLayout {
	Binder<Corso> binder =new Binder<>(Corso.class);
	private Utente docente;
	private CorsoService corsoS;
	private UtenteService utenteS;


	public CorsoForm(CorsoService c,UtenteService u,Utente nomed){
		this.corsoS=c;
		this.utenteS=u;
		this.docente=nomed;

	}
	public void Nuovo(){
		FormLayout formlayout=new FormLayout();
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Button save = new Button();
		Button cancella = new Button("cancella");
		TextField nomeCorso = new TextField("nome Corso");
		TextArea descrizioneCorso = new TextArea("Descrivi brevemente il corso");
		Notification notification = new Notification(
				"è stato aggiunto ti prego di aggiornare la pagina ", 3000,
				Position.TOP_CENTER);
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");

		binder.forField(nomeCorso).withValidator(new StringLengthValidator(
				"Please add the nome", 1, null)).bind(Corso::getNomeCorso,Corso::setNomeCorso);
		binder.forField(descrizioneCorso).bind(Corso::getDescrizioneCorso,Corso::setDescrizioneCorso);
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.setText("save");
		save.addClickListener(e->{
			Corso corso=new Corso();
			corso.setNomeCorso(nomeCorso.getValue().trim());
			corso.setDescrizioneCorso(descrizioneCorso.getValue().toString());
			corso.setDocente(docente);	
			corso.setQuizDelcorso(new ArrayList<Quiz>());
			binder.setBean(corso);
			if((binder.validate().isOk()) && this.corsoS.corsoNonEsistente(corso)){	
				this.corsoS.save(corso);
				this.utenteS.AddCorso(corso,docente);	
				notification.open();
				dialog.close();
				binder.removeBean();				
			}
			else
				Notification.show("error inserire un corso valido");
		});
		cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancella.addClickListener(e->{
			nomeCorso.setValue("");
			descrizioneCorso.setValue("");
			dialog.close();
		});
		formlayout.add(nomeCorso,descrizioneCorso,save,cancella);
		dialog.add(formlayout);
		add(dialog);
		dialog.open();

	}

	public void Modifica(){
		FormLayout formlayout=new FormLayout();
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Button save = new Button();
		Button cancella = new Button("cancella");
		TextField nomeCorso = new TextField("nome Corso");
		TextArea descrizioneCorso = new TextArea("Descrivi brevemente il corso");
		Notification notification = new Notification(
				"è stato modificato ti prego di aggiornare la pagina ", 3000,
				Position.TOP_CENTER);
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		ComboBox<Corso> nomeCorsomodifica= new ComboBox<>();
		nomeCorsomodifica.setLabel("scegli il corso da modificare");
		List<Corso> corsi= this.corsoS.findbyDocente(docente.getNome());
		nomeCorsomodifica.setItemLabelGenerator(Corso::getNomeCorso);
		nomeCorsomodifica.setItems(corsi);
		binder.forField(nomeCorso).withValidator(new StringLengthValidator(
				"Please add the nome", 1, null)).bind(Corso::getNomeCorso,Corso::setNomeCorso);
		binder.forField(descrizioneCorso).bind(Corso::getDescrizioneCorso,Corso::setDescrizioneCorso);
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.setText("modifica");	
		save.addClickListener(e->{
			if((!(nomeCorsomodifica.getValue()==null))){
				Corso corso=nomeCorsomodifica.getValue();
				corso.setNomeCorso(nomeCorso.getValue().trim());
				corso.setDescrizioneCorso(descrizioneCorso.getValue().toString());
				corso.setQuizDelcorso(new ArrayList<Quiz>());
				binder.setBean(corso);
				if((binder.validate().isOk())){	
					this.corsoS.modificaCorso(corso,nomeCorsomodifica.getValue());			
					notification.open();
					dialog.close();
					binder.removeBean();

				}
				else
					Notification.show("error inserire un corso valido");
			}
			else
				Notification.show("error inserire un corso valido");
		});
		cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancella.addClickListener(e->{
			nomeCorso.setValue("");
			descrizioneCorso.setValue("");
			dialog.close();
		});		
		formlayout.add(nomeCorsomodifica,nomeCorso,descrizioneCorso,save,cancella);
		dialog.add(formlayout);
		add(dialog);
		dialog.open();

	}
	public void Elimina() {
		FormLayout formlayout=new FormLayout();
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Button save = new Button();
		Button cancella = new Button("cancella");
		Notification notification = new Notification(
				"è stato eliminato ti prego di aggiornare la pagina ", 3000,
				Position.TOP_CENTER);
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		ComboBox<Corso> nomeCorsomodifica= new ComboBox<>();
		nomeCorsomodifica.setLabel("scegli il corso da eliminare");
		List<Corso> corsi= this.corsoS.findbyDocente(docente.getNome());
		nomeCorsomodifica.setItemLabelGenerator(Corso::getNomeCorso);
		nomeCorsomodifica.setItems(corsi);
		save.addThemeVariants(ButtonVariant.LUMO_ERROR);
		save.setText("elimina");
		save.addClickListener(e->{
			if(!(nomeCorsomodifica.getValue()==null)){
				Corso corso=nomeCorsomodifica.getValue();
				binder.setBean(corso);
				if(binder.validate().isOk()){
					this.utenteS.DeleteCorsoperdoc(nomeCorsomodifica.getValue());
					this.utenteS.DeleteCorso(nomeCorsomodifica.getValue());				
					this.corsoS.elimina(nomeCorsomodifica.getValue());				
					notification.open();
					dialog.close();
					binder.removeBean();
				}
				else{
					Notification.show("error inserire un corso valido");
				}
			}
			else {
				Notification.show("error inserire un corso valido");
			}

		});
		cancella.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		cancella.addClickListener(e->{
			dialog.close();
		});	
		formlayout.add(nomeCorsomodifica,save,cancella);
		dialog.add(formlayout);
		add(dialog);
		dialog.open();
	}
}


