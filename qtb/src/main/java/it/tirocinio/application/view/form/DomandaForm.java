package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.HashSet;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.ui.NotificationConfiguration;

import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.DomandaService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.RispostaService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;
import it.tirocinio.entity.quiz.Risposta;

public class DomandaForm extends FormLayout{


	Binder<Domanda> binder =new Binder<>(Domanda.class);
	private DomandaService domandaS;
	private RispostaService rispostaS;
	private CorsoService corsoS;
	private QuizService quizS;


	public DomandaForm(DomandaService d,CorsoService c,QuizService q,Utente u){
		this.domandaS=d;
		this.corsoS=c;
		this.quizS=q;
	}
	public void Nuovo(Quiz quiz, Grid<Domanda> griddomanda) {
		if(!(quiz==null)){
			VerticalLayout ver=new VerticalLayout();
			Dialog dialog = new Dialog();
			dialog.setWidth("50%");
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			TextField nomeDomanda = new TextField();
			H3 hnome=new H3("Nome : ");
			H3 htesto=new H3("Testo : ");
			TextArea descrizione= new TextArea();
			Button save = new Button("Salva");
			Button cancella = new Button("Cancella");
			binder.forField(nomeDomanda).withValidator(new StringLengthValidator(
					"Please add the nome", 1, null)).bind(Domanda::getNomedomanda,Domanda::setNomedomanda);
			binder.forField(descrizione).withValidator(new StringLengthValidator(
					"Please add the descrizione", 1, null)).bind(Domanda::getDescrizionedomanda,Domanda::setDescrizionedomanda);
			save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			save.addClickListener(e->{
				Domanda domanda = new Domanda();
				domanda.setNomedomanda(nomeDomanda.getValue().trim());
				domanda.setDescrizionedomanda(descrizione.getValue());
				domanda.setRisposte(new ArrayList());
				domanda.setRandomordine(false);
				binder.setBean(domanda);
				if(descrizione.getValue().length()<=255){
					if(binder.validate().isOk()){
						domanda.setQuizapparteneza(quiz);
						quiz.getDomande().add(domanda);
						this.domandaS.save(domanda);
						dialog.close();
						griddomanda.setItems(this.domandaS.findByQuiz(quiz));
						binder.removeBean();
					}
					else
						Notification.show("error inserire una domanda valida");
				}
				else
					
				Notification.show("usare un testo piu breve (max 255 char) attuali : "+descrizione.getValue().length());

			});
			cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
			cancella.addClickListener(e->{
				nomeDomanda.setValue("");
				descrizione.setValue("");
				dialog.close();
			});
			HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
			creaTitoloform(ver,"Crea una domanda");
			creaRigaform(ver,hnome,nomeDomanda);
			creaRigaform(ver,htesto,descrizione);
			ver.add(pulsanti);
			ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
			dialog.add(ver);
			add(dialog);
			dialog.open();
		}
		else
			Notification.show("scegliere un corso prima");
	}

	public void Modifica(Grid<Domanda> griddomanda, Domanda domandav) {
		if(!(domandav==null)){
			if(domandav.getRisposte().isEmpty()){
				VerticalLayout ver = new VerticalLayout();
				Dialog dialog = new Dialog();
				dialog.setWidth("50%");
				dialog.setCloseOnEsc(false);
				dialog.setCloseOnOutsideClick(false);
				Button save = new Button("Salva");
				Button cancella = new Button("Cancella");
				TextField nomeDomanda = new TextField();
				nomeDomanda.setValue(domandav.getNomedomanda());
				H3 hnome = new H3("Nome : ");
				H3 htesto = new H3("Testo : ");
				TextField descrizione= new TextField();
				descrizione.setValue(domandav.getDescrizionedomanda());
				binder.forField(nomeDomanda).withValidator(new StringLengthValidator(
						"Please add the nome", 1, null)).bind(Domanda::getNomedomanda,Domanda::setNomedomanda);
				binder.forField(descrizione).withValidator(new StringLengthValidator(
						"Please add the descrizione", 1, null)).bind(Domanda::getDescrizionedomanda,Domanda::setDescrizionedomanda);
				save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
				save.addClickListener(e->{	
					Domanda domanda=domandav;
					domanda.setNomedomanda(nomeDomanda.getValue());
					domanda.setDescrizionedomanda(descrizione.getValue());
					binder.setBean(domanda);
					if((binder.validate().isOk())){	

						this.domandaS.modificaDomanda(domanda,domandav);	
						griddomanda.setItems(this.domandaS.findByQuiz(domanda.getQuizapparteneza()));
						dialog.close();					
						binder.removeBean();

					}
					else
						Notification.show("error inserire un corso valido");

				});
				cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
				cancella.addClickListener(e->{
					nomeDomanda.setValue("");
					dialog.close();
				});		
				HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
				creaTitoloform(ver,"Modifica la domanda con ID : ",domandav);
				creaRigaform(ver,hnome,nomeDomanda);
				creaRigaform(ver,htesto,descrizione);
				ver.add(pulsanti);
				ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
				dialog.add(ver);
				add(dialog);
				dialog.open();
			}
			else
				Notification.show("Elimina prima le domande per modificare la domanda");
		}
		else
			Notification.show("scegliere una domanda prima");

	}


	public void Elimina(Quiz quiz,Domanda domanda, Grid<Domanda> griddomanda) {
		if(!(domanda==null)){
			VerticalLayout ver = new VerticalLayout();
			Dialog dialog = new Dialog();
			dialog.setWidth("50%");
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			Button save = new Button("Elimina");
			Button cancella = new Button("Cancella");
			save.addThemeVariants(ButtonVariant.LUMO_ERROR);
			save.addClickListener(e->{	
				binder.setBean(domanda);
				if(binder.validate().isOk()){
					if(!(domanda.getRisposte().isEmpty())){
						Notification.show("elimina prima le domande");
					}
					else{
						this.quizS.eliminaDomanda(quiz,domanda);					
						this.domandaS.elimina(domanda);
						griddomanda.setItems(this.domandaS.findByQuiz(quiz));
					}
					dialog.close();
					binder.removeBean();
				}
				else{
					Notification.show("error inserire un corso valido");
				}
			});

			cancella.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
			cancella.addClickListener(e->{
				dialog.close();
			});	
			HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
			creaTitoloform(ver,"Eliminare la domanda con ID : ",domanda);
			ver.add(pulsanti);
			ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
			dialog.add(ver);
			add(dialog);
			dialog.open();
		}
		else
			Notification.show("scegliere un corso prima");
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

	private void creaTitoloform(VerticalLayout ver, String string, Domanda id) {
		H1 h=new H1(string);
		h.add(String.valueOf(id.getId()));
		ver.setHorizontalComponentAlignment(Alignment.CENTER,h);
		ver.add(h);

	}
}


