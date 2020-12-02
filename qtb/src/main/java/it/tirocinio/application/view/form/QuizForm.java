package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;

import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;

public class QuizForm extends FormLayout{

	Binder<Quiz> binder =new Binder<>(Quiz.class);
	private QuizService quizS;
	private CorsoService corsoS;
	private UtenteService utenteS;
	private Utente docente;
	
	
	public QuizForm(QuizService q,CorsoService cs,UtenteService us,Utente u){
		this.quizS=q;
		this.utenteS=us;
		this.docente=u;
		this.corsoS=cs;
	}
	
	public void Nuovo(){
		FormLayout formlayout=new FormLayout();
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		TextField nomeQuiz = new TextField("nome Quiz");
		Button save = new Button("save");
		Button cancella = new Button("cancella");
		NumberField numberField = new NumberField("tempo a disposizione in minuti(se non inserito 60 minuti)");
		ComboBox<Corso> nomeCorso= new ComboBox<>();
		
		Notification notification = new Notification(
		        "è stato aggiunto seleziona il corso in alto", 3000,
		        Position.TOP_CENTER);
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		PopulateBox(nomeCorso);
		binder.forField(nomeQuiz).withValidator(new StringLengthValidator(
				"Please add the nome", 1, null)).bind(Quiz::getNomeQuiz,Quiz::setNomeQuiz);
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.setText("save");
		save.addClickListener(e->{
			Quiz quiz = new Quiz();
			if(numberField.getValue()==null)
			{
				quiz.setTempo("3600");
			}
			else{
				 double tmp=numberField.getValue()*60;
				quiz.setTempo(String.valueOf(tmp));
			}
			quiz.setNomeQuiz(nomeQuiz.getValue().trim());
			quiz.setCorsoAppartenenza(nomeCorso.getValue());
			quiz.setAttivato(false);
			quiz.setDomande(new HashSet<Domanda>());
			binder.setBean(quiz);
			if((binder.validate().isOk())&& this.quizS.quizNonEsistente(quiz) &&!(nomeCorso.getValue()==null)){
			this.quizS.save(quiz);
			notification.open();
			dialog.close();
			binder.removeBean();
			}
			else
				Notification.show("error inserire un quiz valido");

		});
		cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancella.addClickListener(e->{
			nomeQuiz.setValue("");
			dialog.close();
		});
		formlayout.add(nomeCorso,nomeQuiz,numberField,save,cancella);
		dialog.add(formlayout);
		add(dialog);
		dialog.open();
	}
	
	
	private void PopulateBox(ComboBox<Corso> nomeCorso) {
		nomeCorso.setLabel("scegli il corso dove aggiungere il quiz");
		List<Corso> corsi= this.corsoS.findbyDocente(docente);
		nomeCorso.setItemLabelGenerator(Corso::getNomeCorso);
		nomeCorso.setItems(corsi);	
		
	}

	public void Modifica() {
		FormLayout formlayout=new FormLayout();
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Button save = new Button();
		Button cancella = new Button("cancella");
		TextField nomeQuiz = new TextField("nome Quiz");
		TextField id = new TextField("id Test");
		id.setPattern("[0-9.,]*");
		id.setEnabled(false);
		Notification notification = new Notification(
				"è stato modificato ti prego di aggiornare la pagina ", 3000,
				Position.TOP_CENTER);
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		ComboBox<Quiz> nomeQuizmodifica= new ComboBox<>();
		nomeQuizmodifica.setLabel("inserisci in quiz da modificare");
		List<Quiz> quizs=this.quizS.findAllByDocente(docente);
		nomeQuizmodifica.setItemLabelGenerator(Quiz::getNomeQuiz);
		nomeQuizmodifica.setItems(quizs);
		nomeQuizmodifica.addValueChangeListener(e->updateid(nomeQuizmodifica.getValue(),id));
		binder.forField(nomeQuiz).withValidator(new StringLengthValidator(
				"Please add the nome", 1, null)).bind(Quiz::getNomeQuiz,Quiz::setNomeQuiz);
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.setText("modifica");	
		save.addClickListener(e->{
			if((!(nomeQuizmodifica.getValue()==null))){
				Quiz quiz=nomeQuizmodifica.getValue();
				quiz.setNomeQuiz(nomeQuiz.getValue().trim());
				binder.setBean(quiz);
				if((binder.validate().isOk())){	
					this.quizS.modificaQuiz(quiz,nomeQuizmodifica.getValue());		
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
			nomeQuiz.setValue("");
			dialog.close();
		});		
		formlayout.add(nomeQuizmodifica,id,nomeQuiz,save,cancella);
		dialog.add(formlayout);
		add(dialog);
		dialog.open();
	}

	private void updateid(Quiz value, TextField id) {
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
		ComboBox<Quiz> nomeQuizmodifica= new ComboBox<>();
		nomeQuizmodifica.setLabel("scegli il quiz da eliminare");
		List<Quiz> quizs= this.quizS.findAllByDocente(docente);
		nomeQuizmodifica.setItemLabelGenerator(Quiz::getNomeQuiz);
		nomeQuizmodifica.setItems(quizs);
		save.addThemeVariants(ButtonVariant.LUMO_ERROR);
		save.setText("elimina");
		save.addClickListener(e->{
			if(!(nomeQuizmodifica.getValue()==null)){
				Quiz quiz=nomeQuizmodifica.getValue();
				binder.setBean(quiz);
				if(binder.validate().isOk()){
					if(nomeQuizmodifica.getValue().getDomande().isEmpty()){
					this.corsoS.eliminaQuiz(nomeQuizmodifica.getValue());		
					this.quizS.elimina(nomeQuizmodifica.getValue());				
					notification.open();
					dialog.close();
					binder.removeBean();
					}
					else
						Notification.show("elimina prima le domande per favore");
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
		formlayout.add(nomeQuizmodifica,save,cancella);
		dialog.add(formlayout);
		add(dialog);
		dialog.open();
	}
	
}
