package it.tirocinio.application.view.form;

import java.util.HashSet;
import java.util.List;

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


	public  void Nuovo(Quiz quiz) {
		if(!(quiz==null)){
			FormLayout formlayout=new FormLayout();
			Dialog dialog = new Dialog();
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			TextField nomeRisposta = new TextField("Risposta");
			Checkbox giusto = new Checkbox("è giusta?");
			giusto.setValue(false);
			Button save = new Button("save");
			Button cancella = new Button("cancella");
			ComboBox<Domanda> nomeDomanda= new ComboBox<>();
			Notification notification = new Notification(
					"è stato aggiunto ti prego di aggiornare la pagina ", 3000,
					Position.TOP_CENTER);
			addClassName("Reg-view");
			setMaxWidth("500px");
			getStyle().set("margin","0 auto");
			PopulateBox(nomeDomanda,quiz);
			binder.forField(nomeRisposta).withValidator(new StringLengthValidator(
					"Please add the risposta", 1, null)).bind(Risposta::getRisposta,Risposta::setRisposta);
			save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			save.setText("save");
			save.addClickListener(e->{
				Risposta risposta = new Risposta();
				risposta.setRisposta(nomeRisposta.getValue());
				risposta.setDomandaApparteneza(nomeDomanda.getValue());
				risposta.setGiusta(giusto.getValue());
				binder.setBean(risposta);
				if(binder.validate().isOk()){
					this.rispostaS.save(risposta);
					notification.open();
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
			formlayout.add(nomeDomanda,nomeRisposta,giusto,save,cancella);
			dialog.add(formlayout);
			add(dialog);
			dialog.open();
		}
		else
			Notification.show("scegliere un corso prima");
	}


	private void PopulateBox(ComboBox<Domanda> nomeDomanda,Quiz quiz) {
		nomeDomanda.setLabel("scegli su quale domanda");
		List<Domanda> domande= this.domandaS.findByQuiz(quiz);
		nomeDomanda.setItemLabelGenerator(Domanda::getNomedomanda);
		nomeDomanda.setItems(domande);	

	}


	public  void Modifica(Quiz quiz) {
		FormLayout formlayout=new FormLayout();
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Button save = new Button();
		Button cancella = new Button("cancella");
		TextField nomeRisposta = new TextField("Risposta");
		Checkbox giusto = new Checkbox("è giusta?");
		TextField id = new TextField("id Risposta");
		id.setPattern("[0-9.,]*");
		id.setEnabled(false);
		Notification notification = new Notification(
				"è stato modificato ti prego di aggiornare la pagina ", 3000,
				Position.TOP_CENTER);
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		ComboBox<Domanda> domande=new ComboBox<>();
		ComboBox<Risposta> risposte=new ComboBox<>();
		PopulateBox(domande,quiz);
		domande.addValueChangeListener(e->PopulateBoxRisposte(risposte,domande.getValue()));
		binder.forField(nomeRisposta).withValidator(new StringLengthValidator(
				"Please add the risposta", 1, null)).bind(Risposta::getRisposta,Risposta::setRisposta);
		risposte.addValueChangeListener(e->updateid(risposte.getValue(),id));
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.setText("modifica");	
		save.addClickListener(e->{
			if(!(risposte.getValue()==null)){
				Risposta risposta=risposte.getValue();
				risposta.setRisposta(nomeRisposta.getValue());
				risposta.setGiusta(giusto.getValue());
				binder.setBean(risposta);
				if((binder.validate().isOk())){	
					this.rispostaS.modifica(risposta,risposte.getValue());		
					notification.open();
					dialog.close();
					binder.removeBean();
				}
				else
					Notification.show("error inserire una risposta valido");
			}
			else
				Notification.show("error inserire una risposta valido");		
		});
		cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancella.addClickListener(e->{
			nomeRisposta.setValue("");
			dialog.close();
		});		
		formlayout.add(domande,risposte,id,nomeRisposta,giusto,save,cancella);
		dialog.add(formlayout);
		add(dialog);
		dialog.open();

	}




	private void updateid(Risposta value, TextField id) {
		if(value==null){

		}
		else{
			id.setValue(value.getId().toString());
		}
	}


	private void PopulateBoxRisposte(ComboBox<Risposta> risposte, Domanda value) {
		risposte.setLabel("scegli quale risposta");
		if(!(value==null)){
			risposte.setItemLabelGenerator(Risposta::getRisposta);
			risposte.setItems(this.rispostaS.findByDomanda(value));
		}

	}


	public  void Elimina(Quiz quiz) {
		FormLayout formlayout=new FormLayout();
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Button save = new Button();
		Button cancella = new Button("cancella");
		Notification notification = new Notification(
				"è stato modificato ti prego di aggiornare la pagina ", 3000,
				Position.TOP_CENTER);
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		ComboBox<Domanda> domande=new ComboBox<>();
		ComboBox<Risposta> risposte=new ComboBox<>();
		PopulateBox(domande,quiz);
		domande.addValueChangeListener(e->PopulateBoxRisposte(risposte,domande.getValue()));
		save.addThemeVariants(ButtonVariant.LUMO_ERROR);
		save.setText("elimina");	
		save.addClickListener(e->{
			if(!(risposte.getValue()==null)){
				binder.setBean(risposte.getValue());
				if((binder.validate().isOk())){	
					this.domandaS.eliminaRisposta(domande.getValue(),risposte.getValue());
					this.rispostaS.elimina(risposte.getValue());
					notification.open();
					dialog.close();
					binder.removeBean();
				}
				else
					Notification.show("error inserire una risposta valido");
			}
			else
				Notification.show("error inserire una risposta valido");		
		});
		cancella.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		cancella.addClickListener(e->{	
			dialog.close();
		});		
		formlayout.add(domande,risposte,save,cancella);
		dialog.add(formlayout);
		add(dialog);
		dialog.open();
	}
}
