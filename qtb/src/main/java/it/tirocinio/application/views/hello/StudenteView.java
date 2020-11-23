package it.tirocinio.application.views.hello;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.view.form.IscrizioneCorsoForm;
import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Quiz;

@Route(value = "studente", layout = MainView.class)
@PageTitle("studentePage")
@CssImport("./styles/views/hello/hello-view.css")
public class StudenteView extends VerticalLayout {
	private UtenteService utenteS ;
	private String nome ;
	private Utente studente;
	private CorsoService corsoS;
	private IscrizioneCorsoForm iscrizioneCorsoForm;
	private QuizService quizS;
	Grid<Corso> gridcorsi = new Grid<>(Corso.class);
	Grid<Quiz> gridQuiz = new Grid<>(Quiz.class);
	
	
	
	
	
	/**
	 * @param u
	 * @param c
	 */
	public StudenteView(UtenteService u,CorsoService c,QuizService q){
		this.utenteS=u;
		this.quizS=q;
		this.corsoS=c;
		HorizontalLayout hor = new HorizontalLayout();
		Div div1 = new Div();
		Div div2 = new Div();
		setPadding(true);
		  Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    if ( principal instanceof UserDetails){
			  this.nome = ((UserDetails)principal).getUsername();
	    	}
		studente=this.utenteS.findByName(nome);
		List<Corso> corsi=studente.getCorsifrequentati();
		iscrizioneCorsoForm= new IscrizioneCorsoForm(this.corsoS, this.utenteS,studente);
		iscrizioneCorsoForm.setVisible(false);
		hor.add("ciao ");
		hor.add(studente.getNome());
		
		Button buttonaddCorso = new Button("isciviti ad un corso",e->iscrizioneCorsoForm.setVisible(true));
			
		div1.add(hor,buttonaddCorso,iscrizioneCorsoForm);
		div2.setSizeFull();
		div2.add("i tuoi corsi : ");
		HorizontalLayout hor2= new HorizontalLayout();
		hor2.add(gridcorsi,gridQuiz);
		div2.add(hor2);
		configureGridCorsi();
		gridcorsi.setItems(corsi);
		add(div1,div2);
		
	}

	
	private void configureGridCorsi() {
		gridcorsi.removeColumnByKey("id");
		gridcorsi.removeColumnByKey("utentifreq");
		gridcorsi.removeColumnByKey("docente");
		gridcorsi.removeColumnByKey("quizDelcorso");
		
		gridcorsi.setColumns("nomeCorso");	
		gridQuiz.removeColumnByKey("id");
		gridQuiz.setColumns("nomeQuiz");
		gridcorsi.asSingleSelect().addValueChangeListener(event->updateGridQuiz(event.getValue()));
		
	}


	private void updateGridQuiz(Corso value) {
		if(value==null){
			return;
		}
		else
		{

			gridQuiz.setItems(this.quizS.findAllByCorsoandAttivati(value));

		}
	}
	
}
