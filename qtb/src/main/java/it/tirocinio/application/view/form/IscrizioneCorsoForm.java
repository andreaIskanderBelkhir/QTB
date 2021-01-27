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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;


import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;

public class IscrizioneCorsoForm extends FormLayout {
	ComboBox<Corso> nomeCorso= new ComboBox<>();
	private Utente studente;
	private CorsoService corsoS;
	private UtenteService utenteS;
	boolean empty;


	public IscrizioneCorsoForm(CorsoService c,UtenteService u,Utente nomes){
		this.studente=nomes;
		this.utenteS=u;
		this.corsoS=c;
	}


	private void PopulateBox() {
		nomeCorso.setLabel("scegli un corso");
		List<Corso> corsi= this.corsoS.findAll();
		List<Corso> possibili= new ArrayList<Corso>();
		
		for(Corso c: corsi){		
			if((c.getDocente().getCorsifrequentati().contains(c))&&(!(this.corsoS.partecipa(c,studente))&&(this.corsoS.nonRichiesto(c,studente))&&(!(c.getSelezione())))){
				possibili.add(c);
			}
		}
		
		if(possibili.isEmpty()){
			empty=true;
		}
		else{
			empty=false;
		}
		nomeCorso.setItemLabelGenerator(Corso::getNomeCorso);
		nomeCorso.setItems(possibili);	

	}


	public void iscriviti(Grid<Corso> grid,Utente stud) {
		VerticalLayout vert=new VerticalLayout();
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		dialog.setWidth("50%");
		Button save = new Button("Iscriviti");
		Button cancella = new Button("Cancella");
		H3 hcorso=new H3("Scegli il corso : ");
		PopulateBox();
		if(empty){
			nomeCorso.setPlaceholder("Nessun corso disponibile");
			nomeCorso.setEnabled(false);
			Notification.show("nessun corso disponibile");
		}
		else
		{
			
			nomeCorso.setOpened(true);
		}
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickListener(e->{
			if((nomeCorso.getValue()==null)||(this.corsoS.partecipa(nomeCorso.getValue(),studente))){
				Notification.show("inserire un corso valido");
			}
			else{
				corsoS.addStudenteRichiesta(studente, nomeCorso.getValue());
				grid.setItems(stud.getCorsifrequentati());
				Notification.show("il professore è stato avvertito ti prego di attendere");
			}
			dialog.close();

		});
		cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancella.addClickListener(e->{
			dialog.close();
		});
		HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
		creaTitoloform(vert,"Fai domanda per un corso : ");
		creaRigaform(vert,hcorso,nomeCorso);
		vert.add(pulsanti);
		vert.setHorizontalComponentAlignment(Alignment.END,pulsanti);
		dialog.add(vert);
		add(dialog);
		dialog.open();
	}

	private void creaTitoloform(VerticalLayout vert, String string) {
		H1 h=new H1(string);
		vert.setHorizontalComponentAlignment(Alignment.CENTER,h);
		vert.add(h);

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
}
