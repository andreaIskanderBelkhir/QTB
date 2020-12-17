package it.tirocinio.application.views.hello;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.Router;
import com.vaadin.ui.UI;

import it.tirocinio.application.view.form.IscrizioneCorsoForm;
import it.tirocinio.application.views.main.ActionBar;
import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Quiz;
import it.tirocinio.entity.quiz.Risposta;

@Route(value = "studente", layout = MainView.class)
@PageTitle("Corsi dello Studente")
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


	public StudenteView(UtenteService u,CorsoService c,QuizService q){
		this.utenteS=u;
		this.quizS=q;
		this.corsoS=c;

		Div div2 = new Div();
		setSizeFull();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ( principal instanceof UserDetails){
			this.nome = ((UserDetails)principal).getUsername();
		}
		studente=this.utenteS.findByName(nome);

		List<Corso> corsi=studente.getCorsifrequentati();
		configureGridCorsi();
		configureGridQuiz();
		gridcorsi.setItems(corsi);
		iscrizioneCorsoForm= new IscrizioneCorsoForm(this.corsoS, this.utenteS,studente);
		Button buttonaddCorso = new Button("isciviti ad un corso",e->iscrizioneCorsoForm.iscriviti(gridcorsi,studente));
		H3 h=new H3("");
		ActionBar navbar=new ActionBar(buttonaddCorso,h);
		add(navbar);
		div2.setSizeFull();
		div2.add("i miei corsi : ");
		HorizontalLayout hor2= new HorizontalLayout();
		hor2.add(gridcorsi,gridQuiz);
		div2.add(hor2);

		add(iscrizioneCorsoForm,div2);

	}


	private void configureGridQuiz() {
		gridQuiz.setColumns();
		gridQuiz.addColumn(e->{
			return e.getNomeQuiz();
		}).setHeader("Nome test");
		gridQuiz.addComponentColumn(item-> createLink(gridQuiz,item));
		gridQuiz.addColumn(e->{
			return studente.getQuizpassati().contains(e.getId()) ? "Svolto" : "non svolto" ;
		}).setHeader("Stato");

	}


	private H2 createPassato() {
		H2 h=new H2("PASSATO");
		h.getStyle().set("color", "#19bf0a");
		return h;

	}


	private Anchor createLink(Grid<Quiz> gridQuiz2, Quiz item) {
		String a=new String("svolgimento/"+ item.getId().toString());
		Anchor link=new Anchor(a);
		Button button = new Button("Svolgi",new Icon(VaadinIcon.ARROW_FORWARD));
		button.setIconAfterText(true);	
		link.add(button);
		if(studente.getQuizpassati().contains(item.getId())){
			button.setVisible(false);
		}

		return link;
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
