package it.tirocinio.application.view.form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
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
	private Boolean tempModalita;


	public QuizForm(QuizService q,CorsoService cs,UtenteService us,Utente u){
		this.quizS=q;
		this.utenteS=us;
		this.docente=u;
		this.corsoS=cs;
	}

	public void Nuovo(Grid<Quiz> gridquiz, Corso corso){
		if(!(corso==null)){		
			VerticalLayout ver = new VerticalLayout();
			Dialog dialog = new Dialog();
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			dialog.setWidth("50%");
			tempModalita=false;
			H3 hnome=new H3("Nome : ");	
			TextField nomeQuiz = new TextField();
			Button save = new Button("Salva");
			Button cancella = new Button("Cancella");
			H3 htempo=new H3("Tempo a diposizione : ");
			NumberField numberField = new NumberField();
			H3 hgiusto=new H3("Valore risposta esatta : ");
			NumberField numbergiusto = new NumberField();
			H3 hsbagliata=new H3("Valore risposta sbagliata : ");
			NumberField numbersbagliata= new NumberField();
			numberField.setValue((double) 60);
			numbergiusto.setValue((double) 1);
			numbersbagliata.setValue((double) 0.5);
			H3 hsogliaValore=new H3("Valore Soglia : ");
			H3 hsogliaPercentuale=new H3("Valore Soglia (0~100)% : ");
			RadioButtonGroup<String> modalita= new RadioButtonGroup<>();
			modalita.setItems("valore","percentuale");
			modalita.setValue("valore");
			H3 hmodalita =new H3("Modalità di superamento : ");		
			NumberField numberSoglia = new NumberField();
			NumberField numberPercentuale = new NumberField();
			numberPercentuale.setPrefixComponent(new Icon(VaadinIcon.BOOK_PERCENT));
			numberPercentuale.setMin(0);
			numberPercentuale.setMax(100);
			hsogliaPercentuale.setVisible(false);
			numberPercentuale.setVisible(false);
			modalita.addValueChangeListener(e->{
				if(e.getValue().equals("percentuale")){
					tempModalita=true;
					hsogliaPercentuale.setVisible(true);
					numberPercentuale.setVisible(true);
					hsogliaValore.setVisible(false);
					hgiusto.setVisible(false);
					hsbagliata.setVisible(false);
					numberSoglia.setVisible(false);
					numbergiusto.setVisible(false);
					numbersbagliata.setVisible(false);

				}
				else
				{
					tempModalita=false;
					hsogliaPercentuale.setVisible(false);
					numberPercentuale.setVisible(false);
					hsogliaValore.setVisible(true);
					hgiusto.setVisible(true);
					hsbagliata.setVisible(true);
					numberSoglia.setVisible(true);
					numbergiusto.setVisible(true);
					numbersbagliata.setVisible(true);
				}
			});
			binder.forField(nomeQuiz).withValidator(new StringLengthValidator(
					"Please add the nome", 1, null)).bind(Quiz::getNomeQuiz,Quiz::setNomeQuiz);
			save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			save.addClickListener(e->{
				if((!(numberSoglia.isEmpty())) || (!(numberPercentuale.isEmpty()))){
					Quiz quiz = new Quiz();
					if(numberField.getValue()==null)
					{
						quiz.setTempo((double) 60);
					}
					else{
						quiz.setTempo(numberField.getValue());
					}
					quiz.setNomeQuiz(nomeQuiz.getValue().trim());
					quiz.setCorsoAppartenenza(corso);
					quiz.setAttivato(false);
					quiz.setDomande(new HashSet<Domanda>());

					quiz.setValoreGiusta(numbergiusto.getValue());
					quiz.setValoreSbagliata(numbersbagliata.getValue());
					quiz.setModalitaPercentuale(tempModalita);	
					if(tempModalita){
						quiz.setSogliaPercentuale(numberPercentuale.getValue());
					}
					else
						quiz.setSoglia(numberSoglia.getValue());

					binder.setBean(quiz);
					if((binder.validate().isOk())&& this.quizS.quizNonEsistente(quiz)){

						this.quizS.save(quiz);
						gridquiz.setItems(this.quizS.findAllByDocente(this.docente));
						dialog.close();
						binder.removeBean();
					}
					else
						Notification.show("error inserire un quiz valido");
				}

				else
					Notification.show("inserire una soglia");
			});
			cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
			cancella.addClickListener(e->{
				nomeQuiz.setValue("");
				dialog.close();
			});
			HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
			creaTitoloform(ver,"Crea un test");
			creaRigaform(ver,hnome,nomeQuiz);
			creaRigaform(ver,htempo,numberField);
			creaRigaform(ver,hmodalita,modalita);
			creaRigaform(ver, hgiusto,numbergiusto);
			creaRigaform(ver, hsbagliata, numbersbagliata);
			creaRigaform(ver, hsogliaValore,numberSoglia);
			creaRigaform(ver, hsogliaPercentuale,numberPercentuale);

			ver.add(pulsanti);
			ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
			dialog.add(ver);
			add(dialog);
			dialog.open();
		}
		else
			Notification.show("seleziona un corso prima");
	}

	/*
	private void PopulateBox(ComboBox<Corso> nomeCorso) {
		nomeCorso.setLabel("scegli il corso dove aggiungere il quiz");
		List<Corso> corsi= this.corsoS.findbyDocente(docente);
		nomeCorso.setItemLabelGenerator(Corso::getNomeCorso);
		nomeCorso.setItems(corsi);	

	}
	 */
	public void Modifica(Grid<Quiz> gridquiz,Quiz quizv) {
		if(!(quizv==null)){


			VerticalLayout ver=new VerticalLayout();
			Dialog dialog = new Dialog();
			dialog.setWidth("50%");
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			Button save = new Button("Salva");
			Button cancella = new Button("Cancella");
			H3 hnome=new H3("Nome : ");
			H3 htempo=new H3("Tempo a diposizione : ");
			TextField nomeQuiz = new TextField();
			nomeQuiz.setValue(quizv.getNomeQuiz());
			NumberField numberField = new NumberField();	
			H3 hgiusto=new H3("Valore risposta esatta : ");
			NumberField numbergiusto = new NumberField();
			numbergiusto.setValue(quizv.getValoreGiusta());
			H3 hsbagliata=new H3("Valore risposta sbagliata : ");
			NumberField numbersbagliata= new NumberField();
			numbersbagliata.setValue(quizv.getValoreSbagliata());
			H3 hsogliaValore=new H3("Valore Soglia : ");
			H3 hsogliaPercentuale=new H3("Valore Soglia (0~100)% : ");
			RadioButtonGroup<String> modalita= new RadioButtonGroup<>();
			modalita.setItems("valore","percentuale");
			if(quizv.getModalitaPercentuale()){
				modalita.setValue("percentuale");
			}
			else
				modalita.setValue("valore");
			H3 hmodalita =new H3("Modalità di superamento : ");		
			NumberField numberSoglia = new NumberField();
			numberSoglia.setValue(quizv.getSoglia());
			NumberField numberPercentuale = new NumberField();
			numberPercentuale.setValue(quizv.getSogliaPercentuale());
			numberPercentuale.setPrefixComponent(new Icon(VaadinIcon.BOOK_PERCENT));
			numberPercentuale.setMin(0);
			numberPercentuale.setMax(100);
			if(quizv.getModalitaPercentuale()){
				tempModalita=true;
				hsogliaPercentuale.setVisible(true);
				numberPercentuale.setVisible(true);
				hsogliaValore.setVisible(false);
				hgiusto.setVisible(false);
				hsbagliata.setVisible(false);
				numberSoglia.setVisible(false);
				numbergiusto.setVisible(false);
				numbersbagliata.setVisible(false);
			}
			else{
				tempModalita=false;
				hsogliaPercentuale.setVisible(false);
				numberPercentuale.setVisible(false);
				hsogliaValore.setVisible(true);
				hgiusto.setVisible(true);
				hsbagliata.setVisible(true);
				numberSoglia.setVisible(true);
				numbergiusto.setVisible(true);
				numbersbagliata.setVisible(true);

			}
			modalita.addValueChangeListener(e->{
				if(e.getValue().equals("percentuale")){
					tempModalita=true;
					hsogliaPercentuale.setVisible(true);
					numberPercentuale.setVisible(true);
					hsogliaValore.setVisible(false);
					hgiusto.setVisible(false);
					hsbagliata.setVisible(false);
					numberSoglia.setVisible(false);
					numbergiusto.setVisible(false);
					numbersbagliata.setVisible(false);

				}
				else
				{
					tempModalita=false;
					hsogliaPercentuale.setVisible(false);
					numberPercentuale.setVisible(false);
					hsogliaValore.setVisible(true);
					hgiusto.setVisible(true);
					hsbagliata.setVisible(true);
					numberSoglia.setVisible(true);
					numbergiusto.setVisible(true);
					numbersbagliata.setVisible(true);
				}
			});
			numberField.setValue(quizv.getTempo());
			binder.forField(nomeQuiz).withValidator(new StringLengthValidator(
					"Please add the nome", 1, null)).bind(Quiz::getNomeQuiz,Quiz::setNomeQuiz);
			save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			save.addClickListener(e->{
				if((!(numberSoglia.isEmpty())) || (!(numberPercentuale.isEmpty()))){
					Quiz quiz=quizv;
					quiz.setNomeQuiz(nomeQuiz.getValue().trim());
					binder.setBean(quiz);
					if((binder.validate().isOk())){	
						if(numberField.getValue()==null)
						{
							quiz.setTempo((double) 60);
						}
						else{

							quiz.setTempo(numberField.getValue());
						}
						quiz.setValoreGiusta(numbergiusto.getValue());
						quiz.setValoreSbagliata(numbersbagliata.getValue());
						quiz.setModalitaPercentuale(tempModalita);	
						if(tempModalita){
							quiz.setSogliaPercentuale(numberPercentuale.getValue());
						}
						else
							quiz.setSoglia(numberSoglia.getValue());
						this.quizS.modificaQuiz(quiz,quizv);	
						gridquiz.setItems(this.quizS.findAllByDocente(this.docente));
						dialog.close();
						binder.removeBean();

					}
				}
			});
			cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
			cancella.addClickListener(e->{
				nomeQuiz.setValue("");
				dialog.close();
			});		
			HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
			creaTitoloform(ver,"Modifica il quiz con ID : ",quizv);
			creaRigaform(ver,hnome,nomeQuiz);
			creaRigaform(ver,htempo,numberField);
			creaRigaform(ver,hmodalita,modalita);
			creaRigaform(ver, hgiusto,numbergiusto);
			creaRigaform(ver, hsbagliata, numbersbagliata);
			creaRigaform(ver, hsogliaValore,numberSoglia);
			creaRigaform(ver, hsogliaPercentuale,numberPercentuale);
			ver.add(pulsanti);
			ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
			dialog.add(ver);
			add(dialog);
			dialog.open();
		}
		else
			Notification.show("clicca su un test prima");
	}


	public void Elimina(Grid<Quiz> gridquiz,Quiz quizv) {
		if(!(quizv==null)){
			VerticalLayout ver = new VerticalLayout();
			Dialog dialog = new Dialog();
			dialog.setWidth("50%");
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			Button save = new Button("Elimina");
			Button cancella = new Button("Cancella");
			save.addThemeVariants(ButtonVariant.LUMO_ERROR);
			save.addClickListener(e->{
				binder.setBean(quizv);
				if(binder.validate().isOk()){
					if(quizv.getDomande().isEmpty()){
						this.corsoS.eliminaQuiz(quizv);		
						this.quizS.elimina(quizv);				
						dialog.close();
						gridquiz.setItems(this.quizS.findAllByDocente(this.docente));
						binder.removeBean();
					}
					else
						Notification.show("elimina prima le domande per favore");
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
			creaTitoloform(ver,"Eliminare il quiz con ID : ",quizv);
			ver.add(pulsanti);
			ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
			dialog.add(ver);
			add(dialog);
			dialog.open();
		}
		else
			Notification.show("scegli prima un test");
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

	private void creaTitoloform(VerticalLayout ver, String string, Quiz id) {
		H1 h=new H1(string);
		h.add(String.valueOf(id.getId()));
		ver.setHorizontalComponentAlignment(Alignment.CENTER,h);
		ver.add(h);

	}

}
