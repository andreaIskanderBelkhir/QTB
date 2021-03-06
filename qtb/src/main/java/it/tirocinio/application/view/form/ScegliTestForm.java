package it.tirocinio.application.view.form;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.DomandaService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;

public class ScegliTestForm extends FormLayout {
	public QuizService quizS;
	public UtenteService utenteS;
	public DomandaService domandaS;
	public Quiz test;
	Grid<Quiz> gridquiz = new Grid<>(Quiz.class);


	public ScegliTestForm(QuizService q,UtenteService u,DomandaService d){
		this.quizS=q;
		this.utenteS=u;
		this.domandaS=d;
		gridquiz.setColumns("ID");
		gridquiz.addColumn(quiz->{
			return quiz.getNomeQuiz();	}).setHeader("Nome test");
		gridquiz.addColumn(quiz->{
			return quiz.getCorsoAppartenenza().getNomeCorso();
		}).setHeader("Nome corso");
		gridquiz.addColumn(quiz->{
			return quiz.getDomande().size();
		}).setHeader("N° domande");
		gridquiz.addColumn(quiz->{
			return quiz.getTempo();
		}).setHeader("Minuti a disposizione");
		gridquiz.addColumn(quiz->{
			if(quiz.getModalitaPercentuale()){
				return "%";
			}
			else
			return "Valore";
			
		}).setHeader("Modalità");
		gridquiz.addColumn(quiz->{
			if(quiz.getModalitaPercentuale()){
				return quiz.getSogliaPercentuale();
			}
			else
			return quiz.getSoglia();
		}).setHeader("Soglia");
		
		gridquiz.addComponentColumn(item-> createValited(gridquiz,item)).setHeader("Attivato");
		gridquiz.asSingleSelect().addValueChangeListener(event->{
			this.test=event.getValue();
		});
	}
	public void selezionaTest(Utente docente, ComboBox<Quiz> quizs, Grid<Domanda> griddomanda){
		gridquiz.setItems(this.quizS.findAllByDocente(docente));
		VerticalLayout ver=new VerticalLayout();
		Dialog dialog = new Dialog();
		dialog.setWidth("80%");
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Button save = new Button("Scegli");		
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);		
		save.addClickListener(e->{
			if(test!=null){
				quizs.setValue(test);
				griddomanda.setItems(this.domandaS.findByQuiz(test));
				dialog.close();				
			}
		}
				);
		Button cancella = new Button("Cancella");
		cancella.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancella.addClickListener(e->{
			test=null;
			dialog.close();
		});
		HorizontalLayout pulsanti=creazionePulsanti(save, cancella);
		creaTitoloform(ver,"Seleziona Test");		
		ver.add(gridquiz);
		ver.add(pulsanti);
		ver.setHorizontalComponentAlignment(Alignment.END,pulsanti);
		dialog.add(ver);
		add(dialog);
		dialog.open();
	}



	private HorizontalLayout creazionePulsanti(Button save,Button cancella){
		HorizontalLayout pulsanti = new HorizontalLayout();
		H3 h=new H3("");
		pulsanti.add(h,save,cancella);
		pulsanti.setPadding(true);
		pulsanti.expand(h);
		save.getElement().getStyle().set("margin-left", "auto");
		return pulsanti;
	}
	private void creaTitoloform(VerticalLayout vert, String string) {
		H3 h=new H3(string);
		vert.setHorizontalComponentAlignment(Alignment.CENTER,h);
		vert.add(h);
	}
	@SuppressWarnings("unchecked")
	private Checkbox createValited(Grid<Quiz> grid2, Quiz item) {
		Checkbox check = new Checkbox();
		check.setValue(item.getAttivato());
		check.setEnabled(false);
		return check;
	}
}
