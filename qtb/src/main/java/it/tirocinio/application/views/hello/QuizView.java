package it.tirocinio.application.views.hello;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.view.form.DomandaForm;
import it.tirocinio.application.view.form.QuizForm;
import it.tirocinio.application.views.main.ActionBar;
import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.DomandaService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.RispostaService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;

@Route(value = "quizelenco", layout = MainView.class)
@PageTitle("Gestione dei test")
@CssImport("./styles/views/hello/hello-view.css")
public class QuizView extends VerticalLayout{
	private String nome ;
	private Utente docente;
	private CorsoService corsoS;
	private UtenteService utenteS;
	private QuizService quizS;
	private DomandaService domandaS;
	private RispostaService rispostaS;
	Grid<Quiz> gridquiz = new Grid<>(Quiz.class);
	Grid<Domanda> griddomanda= new Grid<>(Domanda.class);
	HorizontalLayout hor=new HorizontalLayout();
	HorizontalLayout hor2=new HorizontalLayout();
	private DomandaForm domandaform;
	private Button creazioneDbutton;

	public QuizView(CorsoService s, UtenteService u,QuizService q,DomandaService d,RispostaService r){

		this.domandaS=d;
		this.rispostaS=r;
		this.corsoS=s;
		this.utenteS=u;
		this.quizS=q;
		setSizeFull();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ( principal instanceof UserDetails){
			this.nome = ((UserDetails)principal).getUsername();
		}	
		docente=this.utenteS.findByName(nome);
		QuizForm quizForm = new QuizForm(this.quizS,this.corsoS,this.docente);
		quizForm.setVisible(false);
		if(!(this.quizS.findAllByDocente(docente).isEmpty())){
			this.domandaform=new DomandaForm(this.domandaS, this.corsoS, docente);
			this.domandaform.setVisible(false);
			this.creazioneDbutton = new Button("Nuova domanda",e->domandaform.setVisible(true));
		}
		Button creazioneQbutton = new Button("Nuovo",e->quizForm.setVisible(true));
		ActionBar navbar2=new ActionBar(creazioneQbutton);
		if(this.creazioneDbutton==null){
			add(navbar2);
		}
		else
			navbar2.AddButtonAtActionBar(this.creazioneDbutton);
		add(navbar2);
		hor.setHeight("100%");
		hor.setWidthFull();
		ConfigureGridQ();
		UpdateGridQ();
		gridquiz.setSizeFull();
		hor.add(gridquiz,quizForm);
		add(hor);
		hor2.setHeight("50%");
		hor2.setWidthFull();
		ConfigureGridD();
		if(this.domandaform==null){
			hor2.add(griddomanda);
		}
		else{
			add("clicca su un quiz per vedere le sue domande");
			hor2.add(griddomanda,domandaform);
		}
		add(hor2);
	}


	private void UpdateGridQ() {
		gridquiz.setItems(this.quizS.findAllByDocente(this.docente));

	}
	private void UpdateGridD(Quiz quiz) {
		griddomanda.setItems(this.domandaS.findByQuiz(quiz));

	}


	private void ConfigureGridQ() {
		gridquiz.setColumns("nomeQuiz");
		gridquiz.setWidth("98%");
		gridquiz.addColumn(quiz->{
			Corso corso= quiz.getCorsoAppartenenza();
			return corso==null ? "-": corso.getNomeCorso();
		}).setHeader("corso del quiz");
		gridquiz.addComponentColumn(item-> createValited(gridquiz,item)).setHeader("attivato");
		gridquiz.asSingleSelect().addValueChangeListener(event->UpdateGridD(event.getValue()));

	}
	private void ConfigureGridD() {
		griddomanda.setColumns("nomedomanda","descrizionedomanda");
		griddomanda.setWidth("98%");
		griddomanda.getColumns().forEach(c->c.setAutoWidth(true));


	}


	@SuppressWarnings("unchecked")
	private Checkbox createValited(Grid<Quiz> grid2, Quiz item) {
		Checkbox check = new Checkbox();
		check.setValue(item.getAttivato());
		check.addClickListener(click->{
			this.quizS.changeValid(item);
		});
		return check;
	}
}
