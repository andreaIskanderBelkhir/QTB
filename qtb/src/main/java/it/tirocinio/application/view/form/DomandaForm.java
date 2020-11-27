package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;

import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.DomandaService;
import it.tirocinio.backend.service.RispostaService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;
import it.tirocinio.entity.quiz.Risposta;

public class DomandaForm extends FormLayout{
	TextField nomeDomanda= new TextField("inserine nome domanda");
	TextArea descrizione = new TextArea("inserire corpo domanda");
	Button save = new Button("save");
	Binder<Domanda> binder =new Binder<>(Domanda.class);
	ComboBox<Quiz> nomeQuiz= new ComboBox<>();
	private DomandaService domandaS;
	private RispostaService rispostaS;
	private CorsoService corsoS;


	public DomandaForm(DomandaService d,CorsoService c,Utente u){
		this.domandaS=d;
		this.corsoS=c;

		Notification notification = new Notification(
				"è stato aggiunto ti prego di aggiornare la pagina o cliccare su un altro corso", 3000,
				Position.TOP_CENTER);
		addClassName("Reg-view");
		setMaxWidth("500px");
		getStyle().set("margin","0 auto");
		PopulateBox(u);
		binder.bindInstanceFields(this);

		binder.forField(nomeDomanda).withValidator(new StringLengthValidator(
				"Please add the nome domanda", 1, null)).bind(Domanda::getNomedomanda,Domanda::setNomedomanda);
		binder.forField(descrizione).withValidator(new StringLengthValidator(
				"Please add the descrizione", 1, null)).bind(Domanda::getDescrizionedomanda,Domanda::setDescrizionedomanda);

		save.addClickListener(e->{
			Domanda domanda = new Domanda();
			domanda.setDescrizionedomanda(descrizione.getValue().trim());
			domanda.setNomedomanda(nomeDomanda.getValue().trim());
			domanda.setQuizapparteneza(nomeQuiz.getValue());
			List<Risposta> risposte=new ArrayList<>();
			domanda.setRisposte(risposte);
			binder.setBean(domanda);
			if((binder.validate().isOk())&& (this.domandaS.domandaNonEsiste(domanda))){
				this.domandaS.save(domanda);
				notification.open();
			}
			else
				Notification.show("error inserire una domanda valida con un nome unico");
		});
		add(nomeQuiz,nomeDomanda,descrizione,save);

	}

	private void PopulateBox(Utente u) {
		nomeQuiz.setLabel("per quale Quiz");
		List<Corso> corsi= this.corsoS.findbyDocente(u.getNome());
		List<Quiz>quiz=new ArrayList<>();
		if(!(corsi.isEmpty())){
			for(Corso c: corsi){		
					for(Quiz q:c.getQuizDelcorso()){
						quiz.add(q);
				}
			}
		}
		nomeQuiz.setItemLabelGenerator(Quiz::getNomeQuiz);
		nomeQuiz.setItems(quiz);
	}

}
