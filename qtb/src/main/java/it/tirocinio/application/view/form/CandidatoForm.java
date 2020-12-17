package it.tirocinio.application.view.form;

import java.util.ArrayList;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;

import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Quiz;

public class CandidatoForm extends HorizontalLayout {
	private CorsoService corsoS;
	private UtenteService utenteS;
	Binder<Utente> binder =new Binder<>(Utente.class);
	
	
	
	
	public CandidatoForm(CorsoService s, UtenteService u){
		this.corsoS=s;
		this.utenteS=u;
	}



	public void Nuovo(Corso corso) {
		if(!(corso==null)){
			VerticalLayout ver=new VerticalLayout();
			Dialog dialog = new Dialog();
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			dialog.setWidth("70%");
			Button save = new Button("Salva");
			save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			Button cancella = new Button("Cancella");
			H3 hnome=new H3("Nome : ");
			TextField nomeCandidato = new TextField();
			H3 hpassword=new H3("Password : ");
			TextField passwordCandidato = new TextField();
			binder.forField(nomeCandidato).withValidator(new StringLengthValidator(
					"Please add the nome", 1, null)).bind(Utente::getNome,Utente::setNome);
			binder.forField(passwordCandidato).withValidator(new StringLengthValidator(
					"Please add a password", 1, null)).bind(Utente::getPassword,Utente::setPassword);
			save.addClickListener(e->{
				Utente utente=new Utente();
				utente.setNome(nomeCandidato.getValue());
				String pas=passwordCandidato.getValue();
				utente.setPassword(this.utenteS.encriptPassword(passwordCandidato.getValue()));
				utente.setRuolo("CANDIDATO");
				utente.setCorsoSelezione(corso);
				binder.setBean(utente);
				if(binder.validate().isOk()){
					this.utenteS.save(utente);
					this.corsoS.addCandidato(utente,corso);
				
					binder.removeBean();
					dialog.close();
					Notification.show("Passa le credenziali al candidato ");
					Notification.show("nome : " + utente.getNome());
					Notification.show("password : "+ pas);
				}
			});
			
			
			cancella.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
			cancella.addClickListener(e->{
				
				dialog.close();
			});	
			HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
			creaTitoloform(ver,"Crea le credenziali per il candidato : ");
			creaRigaform(ver, hnome, nomeCandidato);
			creaRigaform(ver, hpassword, passwordCandidato);
			ver.add(pulsanti);
			ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
			dialog.add(ver);
			add(dialog);
			dialog.open();
			
		}
		else
			Notification.show("Scegli  il tipo di selezione");
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
}
