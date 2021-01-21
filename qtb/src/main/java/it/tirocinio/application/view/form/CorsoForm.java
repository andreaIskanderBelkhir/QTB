package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
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
		dialog.setWidth("50%");
		TextField nomeCorso = new TextField();
		H3 hnome=new H3("Nome : ");
		TextArea descrizioneCorso = new TextArea();
		H3 hdesc =new H3("Descrizione : ");
		Button save = new Button("Salva");
		Button cancella = new Button("Cancella");
		binder.forField(nomeCorso).withValidator(new StringLengthValidator(
				"Please add the nome", 1, null)).bind(Corso::getNomeCorso,Corso::setNomeCorso);
		binder.forField(descrizioneCorso).bind(Corso::getDescrizioneCorso,Corso::setDescrizioneCorso);
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickListener(e->{
			Corso corso=new Corso();
			
			corso.setNomeCorso(nomeCorso.getValue().trim());
			corso.setDescrizioneCorso(descrizioneCorso.getValue().toString());
			corso.setDocente(docente);	
			corso.setQuizDelcorso(new ArrayList<Quiz>());
			corso.setUtentirischiesta(new ArrayList<Long>());
			corso.setSelezione(false);
			binder.setBean(corso);
			if((binder.validate().isOk()) && this.corsoS.corsoNonEsistente(corso)){	
				this.corsoS.save(corso);
				this.utenteS.AddCorso(corso,docente);	
				dialog.close();
				gridtenuti.setItems(this.corsoS.findbyDocente(docente));
				binder.removeBean();				
			}
			else{
				Notification.show("error inserire un corso valido");
				Notification.show("prova con un altro nome");
			}
		});
		cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancella.addClickListener(e->{
			nomeCorso.setValue("");
			descrizioneCorso.setValue("");
			dialog.close();
		});

		HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
		creaTitoloform(vert,"Crea un corso");
		creaRigaform(vert,hnome,nomeCorso);
		creaRigaform(vert,hdesc,descrizioneCorso);
		vert.add(pulsanti);
		vert.setHorizontalComponentAlignment(Alignment.END,pulsanti);
		dialog.add(vert);
		add(dialog);
		dialog.open();
	}

	public void Modifica(Grid<Corso> gridtenuti,Corso corso){
		if(!(corso==null)){
			VerticalLayout ver =new VerticalLayout();
			Dialog dialog = new Dialog();
			dialog.setWidth("50%");
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			H3 hnome=new H3("Nome : ");
			TextField nomeCorso = new TextField();
			nomeCorso.setValue(corso.getNomeCorso());
			H3 hdesc=new H3("Descrizione : ");
			TextArea descrizioneCorso = new TextArea();
			descrizioneCorso.setValue(corso.getDescrizioneCorso());
			Button save = new Button("Salva");
			Button cancella = new Button("Cancella");
			binder.forField(nomeCorso).withValidator(new StringLengthValidator(
					"Please add the nome", 1, null)).bind(Corso::getNomeCorso,Corso::setNomeCorso);
			binder.forField(descrizioneCorso).bind(Corso::getDescrizioneCorso,Corso::setDescrizioneCorso);
			save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);	
			save.addClickListener(e->{
				Corso corsomodificato=corso;
				corsomodificato.setNomeCorso(nomeCorso.getValue().trim());
				corsomodificato.setDescrizioneCorso(descrizioneCorso.getValue().toString());
				binder.setBean(corsomodificato);
				if((binder.validate().isOk())){	
					this.corsoS.modificaCorso(corsomodificato,corso);			
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
			HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
			creaTitoloform(ver,"Modifica il corso con ID : ",corso);
			creaRigaform(ver,hnome,nomeCorso);
			creaRigaform(ver,hdesc,descrizioneCorso);
			ver.add(pulsanti);
			ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
			dialog.add(ver);
			add(dialog);
			dialog.open();
		}
		else
			Notification.show("clicca sul  corso prima");
	}

	public void Elimina(Grid<Corso> gridtenuti,Corso corso) {
		VerticalLayout ver = new VerticalLayout();
		Dialog dialog = new Dialog();
		dialog.setWidth("50%");
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Button save = new Button("Elimina");
		Button cancella = new Button("Cancella");
		save.addThemeVariants(ButtonVariant.LUMO_ERROR);
		save.addClickListener(e->{
			if(!(corso==null)){
				binder.setBean(corso);
				if(binder.validate().isOk()){					
					if(corso.getQuizDelcorso().isEmpty()){
						this.utenteS.DeleteCorsoperdoc(docente,corso);
						this.utenteS.DeleteCorso(corso);				
						this.corsoS.elimina(corso);				
						dialog.close();
						gridtenuti.setItems(this.corsoS.findbyDocente(docente));
						binder.removeBean();
					}
					else
						Notification.show("elimina i quiz prima");
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
		HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
		creaTitoloform(ver,"Eliminare il corso con ID : ",corso);
		ver.add(pulsanti);
		ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
		dialog.add(ver);
		dialog.open();
	}


	HorizontalLayout creazionePulsanti(Button save,Button cancella){
		HorizontalLayout pulsanti = new HorizontalLayout();
		H3 h=new H3("");
		pulsanti.add(h,save,cancella);
		pulsanti.setPadding(true);
		pulsanti.expand(h);
		save.getElement().getStyle().set("margin-left", "auto");
		return pulsanti;
	}


	void creaRigaform(VerticalLayout ver, H3 string, Component component){
		HorizontalLayout h= new HorizontalLayout();
		Div div1=new Div();
		Div div2=new Div();
		h.setWidth("500px");
		h.setMargin(false);
		h.setSpacing(true);
		h.setVerticalComponentAlignment(Alignment.CENTER,div1,div2);    
		h.expand(div1);
		div1.add(string);
		component.getElement().getStyle().set("margin-left", "auto");
		div2.add(component);	
		div1.getElement().getStyle().set("margin-right", "60px");
		h.setAlignItems(Alignment.CENTER);
		h.add(div1,div2);
		ver.add(h);
	}
	private void creaTitoloform(VerticalLayout vert, String string) {
		H1 h=new H1(string);
		vert.setHorizontalComponentAlignment(Alignment.CENTER,h);
		vert.add(h);

	}

	private void creaTitoloform(VerticalLayout ver, String string, Corso corso) {
		H1 h=new H1(string);
		h.add(String.valueOf(corso.getID()));
		ver.setHorizontalComponentAlignment(Alignment.CENTER,h);
		ver.add(h);

	}
	public void Setselezione(Grid<Corso> gridtenuti, Corso corso) {
		if(corso!=null){
			VerticalLayout ver = new VerticalLayout();
			Dialog dialog = new Dialog();
			dialog.setWidth("50%");
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			Button save = new Button("Salva");
			Button cancella = new Button("Cancella");
			save.addThemeVariants(ButtonVariant.LUMO_ERROR);
			save.addClickListener(e->{
				corso.setSelezione(true);
				this.corsoS.save(corso);
				gridtenuti.setItems(this.corsoS.findbyDocente(docente));
				dialog.close();
				Notification.show("Corso verrà usato come selezione");
			});
			cancella.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
			cancella.addClickListener(e->{
				dialog.close();
			});	
			HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
			creaTitoloform(ver,"Usa come selezione : ");
			ver.add(pulsanti);
			ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
			dialog.add(ver);
			dialog.open();
		}
		else
			Notification.show("Scegli un corso prima");
	}
}
