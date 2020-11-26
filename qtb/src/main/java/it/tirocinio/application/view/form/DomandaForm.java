package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
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
	TextField rispostaEsatta =new TextField("inserire risposta giusta");
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
		Notification notification = new Notification(
				"è stato aggiunto ti prego di aggiornare la pagina o cliccare su un altro corso", 3000,
				Position.TOP_CENTER);
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		binder.bindInstanceFields(this);
		binder2.bindInstanceFields(this);

		binder.forField(descrizione).withValidator(new StringLengthValidator(
				"Please add the descrizione", 1, null)).bind(Domanda::getDescrizione,Domanda::setDescrizione);
		binder2.forField(rispostaEsatta).withValidator(new StringLengthValidator(
				"Please add the risposta", 1, null)).bind(Risposta::getDescrizione,Risposta::setDescrizione);
		binder2.forField(rispostasbagliata1).withValidator(new StringLengthValidator(
				"Please add the risposta", 1, null)).bind(Risposta::getDescrizione,Risposta::setDescrizione);
		binder2.forField(rispostasbagliata2).withValidator(new StringLengthValidator(
				"Please add the risposta", 1, null)).bind(Risposta::getDescrizione,Risposta::setDescrizione);
		binder2.forField(rispostasbagliata3).withValidator(new StringLengthValidator(
				"Please add the risposta", 1, null)).bind(Risposta::getDescrizione,Risposta::setDescrizione);
		save.addClickListener(e->{
			Domanda domanda = new Domanda();
			domanda.setDescrizione(descrizione.getValue().trim());
			domanda.setQuizapparteneza(q);
			List<Risposta> risposte=impostaRisposte(domanda);
			domanda.setRisposte(risposte);
			binder.setBean(domanda);
			if((binder2.validate().isOk())&&(binder.validate().isOk())&& (this.domandaS.domandaNonEsiste(domanda))){
				if(domandeUniche()){
					this.domandaS.save(domanda);
					this.rispostaS.save(this.rispostagiusta);
					this.rispostaS.save(this.risposta1);
					this.rispostaS.save(this.risposta2);
					this.rispostaS.save(this.risposta3);
					notification.open();
				}
				else
					Notification.show("alcune risposte sono uguali, cambiale");
			}
			else
				Notification.show("error inserire una domanda valida con una descrizione unica");
		});

		add(descrizione,rispostaEsatta,rispostasbagliata1,rispostasbagliata2,rispostasbagliata3,save);

	}

	private boolean domandeUniche() {
		boolean b=true;
		if( this.risposta1.getDescrizione().equals(this.risposta2.getDescrizione())){
			b=false;
		}
		if( this.risposta3.getDescrizione().equals(this.risposta2.getDescrizione())){
			b=false;
		}
		if( this.risposta1.getDescrizione().equals(this.rispostagiusta.getDescrizione())){
			b=false;
		}
		if( this.risposta1.getDescrizione().equals(this.risposta3.getDescrizione())){
			b=false;
		}
		if( this.risposta3.getDescrizione().equals(this.rispostagiusta.getDescrizione())){
			b=false;
		}
		if( this.rispostagiusta.getDescrizione().equals(this.risposta2.getDescrizione())){
			b=false;
		}
		return b;
	}

	private  List<Risposta> impostaRisposte(Domanda domanda) {
		List<Risposta> risposte= new ArrayList<Risposta>();
		this.rispostagiusta=new Risposta();
		this.rispostagiusta.setDescrizione(rispostaEsatta.getValue().trim());
		this.rispostagiusta.setDomandaApparteneza(domanda);
		this.rispostagiusta.setGiusta(true);
		risposte.add(this.rispostagiusta);
		this.risposta1=new Risposta();
		risposta1.setDescrizione(rispostasbagliata1.getValue().trim());
		risposta1.setDomandaApparteneza(domanda);
		risposta1.setGiusta(false);
		risposte.add(risposta1);
		this.risposta2=new Risposta();
		risposta2.setDescrizione(rispostasbagliata2.getValue().trim());
		risposta2.setDomandaApparteneza(domanda);
		risposta2.setGiusta(false);
		risposte.add(risposta2);
		this.risposta3=new Risposta();
		risposta3.setDescrizione(rispostasbagliata3.getValue().trim());
		risposta3.setDomandaApparteneza(domanda);
		risposta3.setGiusta(false);
		risposte.add(risposta3);
		return risposte;



	}
}
