package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;

import it.tirocinio.backend.service.DomandaService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.RispostaService;
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;
import it.tirocinio.entity.quiz.Risposta;

public class DomandaForm extends FormLayout{
	TextArea descrizione = new TextArea("Descrizione domanda");
	TextField rispostaGiusta =new TextField("inserire risposta giusta");
	TextField rispostasbagliata1 =new TextField("inserire risposta sbagliata 1");
	TextField rispostasbagliata2 =new TextField("inserire risposta sbagliata 2");
	TextField rispostasbagliata3 =new TextField("inserire risposta sbagliata 3");
	Button save = new Button("save");
	Binder<Domanda> binder =new Binder<>(Domanda.class);
	Binder<Risposta> binder2 =new Binder<>(Risposta.class);
	private DomandaService domandaS;
	private RispostaService rispostaS;
	private Risposta risposta1;
	private Risposta risposta2;
	private Risposta risposta3;
	private Risposta rispostagiusta;
	
	public DomandaForm(DomandaService d,RispostaService r,Quiz q){
		this.domandaS=d;
		this.rispostaS=r;
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		binder.bindInstanceFields(this);
		binder2.bindInstanceFields(this);
		
		binder.forField(descrizione).withValidator(new StringLengthValidator(
				"Please add the descrizione", 1, null)).bind(Domanda::getDescrizione,Domanda::setDescrizione);
		binder2.forField(rispostaGiusta).withValidator(new StringLengthValidator(
				"Please add the risposta", 1, null)).bind(Risposta::getDescrizione,Risposta::setDescrizione);
		binder2.forField(rispostasbagliata1).withValidator(new StringLengthValidator(
				"Please add the risposta", 1, null)).bind(Risposta::getDescrizione,Risposta::setDescrizione);
		binder2.forField(rispostasbagliata2).withValidator(new StringLengthValidator(
				"Please add the risposta", 1, null)).bind(Risposta::getDescrizione,Risposta::setDescrizione);
		binder2.forField(rispostasbagliata3).withValidator(new StringLengthValidator(
				"Please add the risposta", 1, null)).bind(Risposta::getDescrizione,Risposta::setDescrizione);
		save.addClickListener(e->{
			Domanda domanda = new Domanda();
			domanda.setDescrizione(descrizione.getValue());
			domanda.setQuizapparteneza(q);
			List<Risposta> risposte=impostaRisposte(domanda);
			domanda.setRisposte(risposte);
			binder.setBean(domanda);
			if(binder.validate().isOk()){
				this.domandaS.save(domanda);
				this.rispostaS.save(this.rispostagiusta);
				this.rispostaS.save(this.risposta1);
				this.rispostaS.save(this.risposta2);
				this.rispostaS.save(this.risposta3);
			}
		});
		
		add(descrizione,rispostaGiusta,rispostasbagliata1,rispostasbagliata2,rispostasbagliata3,save);
		
	}

	private  List<Risposta> impostaRisposte(Domanda domanda) {
		List<Risposta> risposte= new ArrayList<Risposta>();
		this.rispostagiusta=new Risposta();
		rispostagiusta.setDescrizione(rispostagiusta.getDescrizione());
		rispostagiusta.setDomandaApparteneza(domanda);
		rispostagiusta.setGiusta(true);
		risposte.add(rispostagiusta);
		this.risposta1=new Risposta();
		risposta1.setDescrizione(rispostasbagliata1.getValue());
		risposta1.setDomandaApparteneza(domanda);
		risposta1.setGiusta(false);
		risposte.add(risposta1);
		this.risposta2=new Risposta();
		risposta2.setDescrizione(rispostasbagliata2.getValue());
		risposta2.setDomandaApparteneza(domanda);
		risposta2.setGiusta(false);
		risposte.add(risposta2);
		this.risposta3=new Risposta();
		risposta3.setDescrizione(rispostasbagliata3.getValue());
		risposta3.setDomandaApparteneza(domanda);
		risposta3.setGiusta(false);
		risposte.add(risposta3);
		return risposte;
		
		
		
	}
}
