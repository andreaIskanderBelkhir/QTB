package it.tirocinio.application.view.form;

import java.util.HashSet;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;

import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.DomandaService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.RispostaService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;
import it.tirocinio.entity.quiz.Risposta;

public class RispostaForm extends FormLayout{
	Binder<Risposta> binder =new Binder<>(Risposta.class);
	private QuizService quizS;
	private CorsoService corsoS;
	private UtenteService utenteS;
	private DomandaService domandaS;
	private RispostaService rispostaS;


	public RispostaForm(QuizService q,CorsoService cs,DomandaService d,RispostaService r,UtenteService us){
		this.quizS=q;
		this.rispostaS=r;
		this.domandaS=d;
		this.utenteS=us;
		this.corsoS=cs;
	}


	public  void Nuovo(Quiz quiz, Domanda domanda, Grid<Risposta> gridrisposta) {
		if((!(quiz==null))&&(!(domanda==null))){
			VerticalLayout ver = new VerticalLayout();
			Dialog dialog = new Dialog();
			dialog.setWidth("50%");
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			TextField nomeRisposta = new TextField();
			H3 hnome = new H3("Valore : ");
			H3 hgiusto=new H3("Giusta ? ");
			Checkbox giusto = new Checkbox();
			giusto.setValue(false);
			Button save = new Button("Salva");
			Button cancella = new Button("Cancella");
			binder.forField(nomeRisposta).withValidator(new StringLengthValidator(
					"Please add the risposta", 1, null)).bind(Risposta::getRisposta,Risposta::setRisposta);
			save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			save.addClickListener(e->{
				Risposta risposta = new Risposta();
				risposta.setRisposta(nomeRisposta.getValue());
				risposta.setDomandaApparteneza(domanda);
				risposta.setGiusta(giusto.getValue());
				domanda.getRisposte().add(risposta);
				binder.setBean(risposta);
				if(binder.validate().isOk()){
					this.rispostaS.save(risposta);
					gridrisposta.setItems(this.domandaS.findRisposte(domanda));
					dialog.close();
					binder.removeBean();
				}
				else
					Notification.show("error inserire un quiz valido");

			});
			cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
			cancella.addClickListener(e->{
				nomeRisposta.setValue("");
				dialog.close();
			});
			HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
			creaTitoloform(ver,"Crea una risposta");
			creaRigaform(ver,hnome,nomeRisposta);
			creaRigaform(ver,hgiusto,giusto);
			ver.add(pulsanti);
			ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
			dialog.add(ver);
			add(dialog);
			dialog.open();
		}
		else
			Notification.show("scegliere un test e una domanda prima");
	}



	public  void Modifica(Quiz quiz, Risposta rispostavecchia, Grid<Risposta> gridrisposta) {
		if(!(rispostavecchia==null)){
		VerticalLayout ver = new VerticalLayout();
		Dialog dialog = new Dialog();
		dialog.setWidth("50%");
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Button save = new Button("Salva");
		Button cancella = new Button("Cancella");
		H3 hnome =new H3("Nome : ");
		H3 hgiusto=new H3("Giusta ? ");
		TextField nomeRisposta = new TextField();
		nomeRisposta.setValue(rispostavecchia.getRisposta());
		Checkbox giusto = new Checkbox();
		giusto.setValue(rispostavecchia.getGiusta());
		binder.forField(nomeRisposta).withValidator(new StringLengthValidator(
				"Please add the risposta", 1, null)).bind(Risposta::getRisposta,Risposta::setRisposta);
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickListener(e->{			
				Risposta risposta=rispostavecchia;
				risposta.setRisposta(nomeRisposta.getValue());
				risposta.setGiusta(giusto.getValue());
				binder.setBean(risposta);
				if((binder.validate().isOk())){	
					this.rispostaS.modifica(risposta,rispostavecchia);	
					gridrisposta.setItems(this.domandaS.findRisposte(risposta.getDomandaApparteneza()));
					dialog.close();
					binder.removeBean();
				}
				else
					Notification.show("error inserire una risposta valido");	
		});
		cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancella.addClickListener(e->{
			nomeRisposta.setValue("");
			dialog.close();
		});		
		HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
		creaTitoloform(ver,"Modifica la risposta con ID : ",rispostavecchia);
		creaRigaform(ver,hnome,nomeRisposta);
		creaRigaform(ver,hgiusto,giusto);
		ver.add(pulsanti);
		ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
		dialog.add(ver);
		add(dialog);
		dialog.open();
		}
		else
			Notification.show("clicca su una risposta prima");
	}

	public  void Elimina(Grid<Risposta> gridrisposta,Risposta risposta) {
		if(!(risposta==null)){
		VerticalLayout ver = new VerticalLayout();
		Dialog dialog = new Dialog();
		dialog.setWidth("50%");
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Button save = new Button("Elimina");
		Button cancella = new Button("Cancella");
		save.addThemeVariants(ButtonVariant.LUMO_ERROR);
		save.addClickListener(e->{
				binder.setBean(risposta);
				if((binder.validate().isOk())){	
					this.domandaS.eliminaRisposta(risposta.getDomandaApparteneza(),risposta);
					this.rispostaS.elimina(risposta);
					gridrisposta.setItems(this.domandaS.findRisposte(risposta.getDomandaApparteneza()));
					dialog.close();
					binder.removeBean();
				}
				else
					Notification.show("error inserire una risposta valido");	
		});
		cancella.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		cancella.addClickListener(e->{	
			dialog.close();
		});		
		HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
		creaTitoloform(ver,"Eliminare la risposta con ID : ",risposta);
		ver.add(pulsanti);
		ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
		dialog.add(ver);
		add(dialog);
		dialog.open();
		}
		else
			Notification.show("scegli la risposta prima");
	}
	public  void Elimina(Risposta risposta) {
		if(!(risposta==null)){
		VerticalLayout ver = new VerticalLayout();
		Dialog dialog = new Dialog();
		dialog.setWidth("50%");
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Button save = new Button("Elimina");
		Button cancella = new Button("Cancella");
		save.addThemeVariants(ButtonVariant.LUMO_ERROR);
		save.addClickListener(e->{
				binder.setBean(risposta);
				if((binder.validate().isOk())){	
					this.domandaS.eliminaRisposta(risposta.getDomandaApparteneza(),risposta);
					this.rispostaS.elimina(risposta);
					
					dialog.close();
					binder.removeBean();
				}
				else
					Notification.show("error inserire una risposta valido");	
		});
		cancella.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		cancella.addClickListener(e->{	
			dialog.close();
		});		
		HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
		creaTitoloform(ver,"Eliminare la risposta con ID : ",risposta);
		ver.add(pulsanti);
		ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
		dialog.add(ver);
		add(dialog);
		dialog.open();
		}
		else
			Notification.show("scegli la risposta prima");
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

	private void creaTitoloform(VerticalLayout ver, String string, Risposta id) {
		H1 h=new H1(string);
		h.add(String.valueOf(id.getId()));
		ver.setHorizontalComponentAlignment(Alignment.CENTER,h);
		ver.add(h);

	}
}
