package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

	private void PopulateBox(Utente u,ComboBox<Quiz> nomeQuiz) {
		nomeQuiz.setLabel("per quale Quiz");
		List<Corso> corsi= this.corsoS.findbyDocente(u);
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

	public void Nuovo(Quiz quiz) {
		if(!(quiz==null)){
			FormLayout formlayout=new FormLayout();
			Dialog dialog = new Dialog();
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			TextField nomeDomanda = new TextField("nome Domanda");
			TextField descrizione= new TextField("Testo Domanda");
			Button save = new Button("save");
			Button cancella = new Button("cancella");
			
			Notification notification = new Notification(
					"è stato aggiunto ti prego di aggiornare la pagina o cliccare su un altro corso", 3000,
					Position.TOP_CENTER);
			addClassName("Reg-view");
			setMaxWidth("500px");
			getStyle().set("margin","0 auto");
			
			binder.forField(nomeDomanda).withValidator(new StringLengthValidator(
					"Please add the nome", 1, null)).bind(Domanda::getNomedomanda,Domanda::setNomedomanda);
			binder.forField(descrizione).withValidator(new StringLengthValidator(
					"Please add the descrizione", 1, null)).bind(Domanda::getDescrizionedomanda,Domanda::setDescrizionedomanda);
			save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			save.setText("save");
			save.addClickListener(e->{
				Domanda domanda = new Domanda();
				domanda.setNomedomanda(nomeDomanda.getValue().trim());
				domanda.setDescrizionedomanda(descrizione.getValue());
				domanda.setQuizapparteneza(quiz);
				domanda.setRisposte(new HashSet<>());
				binder.setBean(domanda);
				if(binder.validate().isOk()){
					this.domandaS.save(domanda);
					notification.open();
					dialog.close();
				}
				else
					Notification.show("error inserire una domanda valido");

			});
			cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
			cancella.addClickListener(e->{
				nomeDomanda.setValue("");
				descrizione.setValue("");
				dialog.close();
			});
			formlayout.add(nomeDomanda,descrizione,save,cancella);
			dialog.add(formlayout);
			add(dialog);
			dialog.open();
		}
		else
			Notification.show("scegliere un corso prima");
	}

	public void Modifica(Quiz quiz) {
		if(!(quiz==null)){
			FormLayout formlayout=new FormLayout();
			Dialog dialog = new Dialog();
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			Button save = new Button();
			Button cancella = new Button("cancella");
			TextField nomeDomanda = new TextField("nome Domanda");
			TextField descrizione= new TextField("Testo Domanda");
			TextField id = new TextField("id Domanda");
			id.setPattern("[0-9.,]*");
			id.setEnabled(false);
			Notification notification = new Notification(
					"è stato modificato ti prego di aggiornare la pagina ", 3000,
					Position.TOP_CENTER);
			addClassName("Reg-view");
			setMaxWidth("500px");
			getStyle().set("margin","0 auto");
			ComboBox<Domanda> nomeDomandamodifica= new ComboBox<>();
			nomeDomandamodifica.setLabel("inserisci la domanda da modificare");
			List<Domanda> domande=this.domandaS.findByQuiz(quiz);
			nomeDomandamodifica.setItemLabelGenerator(Domanda::getNomedomanda);
			nomeDomandamodifica.setItems(domande);
			nomeDomandamodifica.addValueChangeListener(e->updateid(nomeDomandamodifica.getValue(),id));
			binder.forField(nomeDomanda).withValidator(new StringLengthValidator(
					"Please add the nome", 1, null)).bind(Domanda::getNomedomanda,Domanda::setNomedomanda);
			binder.forField(descrizione).withValidator(new StringLengthValidator(
					"Please add the descrizione", 1, null)).bind(Domanda::getDescrizionedomanda,Domanda::setDescrizionedomanda);
			save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			save.setText("modifica");	
			save.addClickListener(e->{
				if((!(nomeDomandamodifica.getValue()==null))){
					Domanda domanda=nomeDomandamodifica.getValue();
					domanda.setNomedomanda(nomeDomanda.getValue());
					domanda.setDescrizionedomanda(descrizione.getValue());
					binder.setBean(domanda);
					if((binder.validate().isOk())){	
						this.domandaS.modificaDomanda(domanda,nomeDomandamodifica.getValue());		
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
				nomeDomanda.setValue("");
				dialog.close();
			});		
			formlayout.add(nomeDomandamodifica,id,nomeDomanda,descrizione,save,cancella);
			dialog.add(formlayout);
			add(dialog);
			dialog.open();
		}
		else
			Notification.show("scegliere un corso prima");

	}

	private void updateid(Domanda value, TextField id) {
		if(value==null){
			
		}
		else{
			id.setValue(value.getId().toString());
		}
	}

	public void Elimina(Quiz quiz) {
		if(!(quiz==null)){
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
			ComboBox<Domanda> nomeDomandamodifica= new ComboBox<>();
			nomeDomandamodifica.setLabel("scegli la domanda da eliminare");
			List<Domanda> domande= this.domandaS.findByQuiz(quiz);
			nomeDomandamodifica.setItemLabelGenerator(Domanda::getNomedomanda);
			nomeDomandamodifica.setItems(domande);
			save.addThemeVariants(ButtonVariant.LUMO_ERROR);
			save.setText("elimina");
			save.addClickListener(e->{
				if(!(nomeDomandamodifica.getValue()==null)){
					binder.setBean(nomeDomandamodifica.getValue());
					if(binder.validate().isOk()){
						this.quizS.eliminaDomanda(quiz,nomeDomandamodifica.getValue());
						this.domandaS.elimina(nomeDomandamodifica.getValue());
						notification.open();
						dialog.close();
						binder.removeBean();
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
			formlayout.add(nomeDomandamodifica,save,cancella);
			dialog.add(formlayout);
			add(dialog);
			dialog.open();
		}
	

	else
		Notification.show("scegliere un corso prima");
}

}
