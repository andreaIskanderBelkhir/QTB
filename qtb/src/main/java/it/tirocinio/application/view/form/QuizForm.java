package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;

import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;

public class QuizForm extends FormLayout{
	TextField nomeQuiz = new TextField("nome Quiz");
	Button save = new Button("save");
	ComboBox<Corso> nomeCorso= new ComboBox<>();
	Binder<Quiz> binder =new Binder<>(Quiz.class);
	private QuizService quizS;
	private CorsoService corsoS;
	private Utente docente;
	
	
	public QuizForm(QuizService q,CorsoService cs,Utente u){
		this.quizS=q;
		this.docente=u;
		this.corsoS=cs;
		Notification notification = new Notification(
		        "è stato aggiunto ti prego di aggiornare la pagina o cliccare su un altro corso", 3000,
		        Position.TOP_CENTER);
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		PopulateBox();
		binder.bindInstanceFields(this);
		binder.forField(nomeQuiz).withValidator(new StringLengthValidator(
				"Please add the nome", 1, null)).bind(Quiz::getNomeQuiz,Quiz::setNomeQuiz);
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickListener(e->{
			Quiz quiz = new Quiz();
			quiz.setNomeQuiz(nomeQuiz.getValue().trim());
			quiz.setCorsoAppartenenza(nomeCorso.getValue());
			quiz.setAttivato(false);
			quiz.setDomande(new ArrayList<Domanda>());
			binder.setBean(quiz);
			if((binder.validate().isOk())&& this.quizS.quizNonEsistente(quiz) &&!(nomeCorso.getValue()==null)){
			this.quizS.save(quiz);
			notification.open();
			}
			else
				Notification.show("error inserire un quiz valido");

		});
		add(nomeCorso,nomeQuiz,save);
	}
	
	
	private void PopulateBox() {
		nomeCorso.setLabel("scegli il corso dove aggiungere il quiz");
		List<Corso> corsi= this.corsoS.findbyDocente(docente.getNome());
		nomeCorso.setItemLabelGenerator(Corso::getNomeCorso);
		nomeCorso.setItems(corsi);	
		
	}
}
