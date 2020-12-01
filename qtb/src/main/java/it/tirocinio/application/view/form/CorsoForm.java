package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.NumberField;
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

public class CorsoForm extends HorizontalLayout {
	Binder<Corso> binder =new Binder<>(Corso.class);
	private Utente docente;
	private CorsoService corsoS;
	private UtenteService utenteS;


	public CorsoForm(CorsoService c,UtenteService u,Utente nomed){
		this.corsoS=c;
		this.utenteS=u;
		this.docente=nomed;

	}
	public void Nuovo(Grid<Corso> gridtenuti){
		VerticalLayout vert=new VerticalLayout();
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Dialog dialogchiusura = new Dialog();
		dialogchiusura.setCloseOnEsc(false);
		dialogchiusura.setCloseOnOutsideClick(false);
		H1 h1 = new H1("E' necessario riavviare la pagina");
		dialogchiusura.add(h1);
		Button save = new Button();
		Button cancella = new Button("cancella");
		TextField nomeCorso = new TextField();
		H3 hnome=new H3("inserisci nome corso : ");
		HorizontalLayout hor1=new HorizontalLayout();
		TextArea descrizioneCorso = new TextArea();
		H3 hdesc =new H3("inserisci descrizione corso : ");
		HorizontalLayout hor2=new HorizontalLayout();
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
				dialog.close();
				gridtenuti.setItems(this.corsoS.findbyDocente(docente));
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
		HorizontalLayout pulsanti = new HorizontalLayout();
		H3 h=new H3("");
		pulsanti.add(h,save,cancella);
		pulsanti.setPadding(true);
		pulsanti.expand(h);
		save.getElement().getStyle().set("margin-left", "auto");
		
		
		hor1.add(hnome,nomeCorso);
		hor1.setVerticalComponentAlignment(Alignment.CENTER,hnome,nomeCorso);
		hor2.add(hdesc,descrizioneCorso);
		vert.add(hor1,hor2,pulsanti);
		vert.setHorizontalComponentAlignment(Alignment.END,pulsanti);
	
		dialog.add(vert);
		add(dialog,dialogchiusura);
		dialog.open();
	}

	public void Modifica(){
		FormLayout formlayout=new FormLayout();
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Button save = new Button();
		Button cancella = new Button("cancella");
		TextField id = new TextField("id corso");
		id.setPattern("[0-9.,]*");
		id.setEnabled(false);
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
		List<Corso> corsi= this.corsoS.findbyDocente(docente);
		nomeCorsomodifica.setItemLabelGenerator(Corso::getNomeCorso);
		nomeCorsomodifica.setItems(corsi);
		nomeCorsomodifica.addValueChangeListener(e->updateid(nomeCorsomodifica.getValue(),id));
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
		formlayout.add(nomeCorsomodifica,id,nomeCorso,descrizioneCorso,save,cancella);
		dialog.add(formlayout);
		add(dialog);
		dialog.open();

	}
	private void updateid(Corso value, TextField id) {
		if(value==null){
			
		}
		else{
			id.setValue(value.getId().toString());
		}
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
		List<Corso> corsi= this.corsoS.findbyDocente(docente);
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


