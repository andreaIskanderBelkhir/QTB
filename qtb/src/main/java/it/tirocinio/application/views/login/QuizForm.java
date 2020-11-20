package it.tirocinio.application.views.login;

import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;

import it.tirocinio.backend.service.QuizService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Quiz;

public class QuizForm extends FormLayout{
	TextField nomeQuiz = new TextField("nome Quiz");
	Button save = new Button("save");
	Binder<Quiz> binder =new Binder<>(Quiz.class);
	private QuizService quizS;
	
	
	public QuizForm(QuizService q,Corso c){
		this.quizS=q;
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		binder.bindInstanceFields(this);
		binder.forField(nomeQuiz).withValidator(new StringLengthValidator(
				"Please add the nome", 1, null)).bind(Quiz::getNomeQuiz,Quiz::setNomeQuiz);
		save.addClickListener(e->{
			Quiz quiz = new Quiz();
			quiz.setNomeQuiz(nomeQuiz.getValue());
			quiz.setCorsoAppertenenza(c);
			quiz.setAttivato(true);
			binder.setBean(quiz);
			if(binder.validate().isOk())
			this.quizS.save(quiz);

		});
		add(nomeQuiz,save);
	}
}
