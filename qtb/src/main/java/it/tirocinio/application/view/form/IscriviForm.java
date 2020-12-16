package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
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

public class IscriviForm extends FormLayout{
	private CorsoService corsoS;
	private UtenteService utenteS;
	Grid<Utente> gridUtentinuovi = new Grid<>(Utente.class);



	public IscriviForm(CorsoService s, UtenteService u){
		this.corsoS=s;
		this.utenteS=u;
		gridUtentinuovi.setColumns("id");
		gridUtentinuovi.addColumn(utente->{
			return utente.getNome();
		}).setHeader("Nome");
	}



	public void Iscrivi(Corso corso, Grid<Utente> gridUtentivecchio) {
		if(!(corso==null)){	
			List<Utente> ut=this.utenteS.findStudenti();
			ut.removeAll(this.utenteS.findByCorso(corso));
			gridUtentinuovi.setItems(ut);			
			gridUtentinuovi.setSelectionMode(SelectionMode.MULTI);
			VerticalLayout ver=new VerticalLayout();
			Dialog dialog = new Dialog();
			dialog.setWidth("50%");
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			Button save = new Button("Iscrivi");		
			save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);			
			save.addClickListener(e->{
				for(Utente u:gridUtentinuovi.getSelectedItems()){
					this.corsoS.addStudente(u, corso);
					this.utenteS.AddCorso(corso, u);
				}
				gridUtentivecchio.setItems(this.utenteS.findByCorso(corso));
				dialog.close();
			});
			Button cancella = new Button("Cancella");
			cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
			cancella.addClickListener(e->{
				dialog.close();
			});

			HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
			creaTitoloform(ver,"Seleziona Utenti da Iscrivere");		
			ver.add(gridUtentinuovi);
			ver.add(pulsanti);
			ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
			dialog.add(ver);
			add(dialog);
			dialog.open();
		}
		else
			Notification.show("Seleziona un corso prima");
	}



	private HorizontalLayout creazionePulsanti(Button save,Button cancella){
		HorizontalLayout pulsanti = new HorizontalLayout();
		H3 h=new H3("");
		pulsanti.add(h,save,cancella);
		pulsanti.setPadding(true);
		pulsanti.expand(h);
		save.getElement().getStyle().set("margin-left", "auto");
		return pulsanti;
	}
	private void creaTitoloform(VerticalLayout vert, String string) {
		H1 h=new H1(string);
		vert.setHorizontalComponentAlignment(Alignment.CENTER,h);
		vert.add(h);
	}
}
