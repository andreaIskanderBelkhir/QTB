package it.tirocinio.application.views.hello;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
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
	private Quiz quiz;
	Grid<Quiz> gridquiz = new Grid<>(Quiz.class);
	HorizontalLayout hor=new HorizontalLayout();
	HorizontalLayout hor2=new HorizontalLayout();
	ComboBox<Corso> corsi;


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
		QuizForm quizForm = new QuizForm(this.quizS,this.corsoS,this.utenteS,this.docente);
		corsi=new ComboBox<>();
		corsi.setItemLabelGenerator(Corso::getNomeCorso);
		corsi.setItems(this.corsoS.findbyDocente(docente));
		corsi.addValueChangeListener(e->{
			UpdateGridQ();
		});
		Button creazioneQbutton = new Button("Nuovo",e->quizForm.Nuovo(gridquiz,corsi.getValue()));
		Button modificaQbutton = new Button("Modifica",e->quizForm.Modifica(gridquiz,quiz));
		Button eliminaQbutton = new Button("Elimina",e->quizForm.Elimina(gridquiz, quiz));
		ActionBar navbar2=new ActionBar(creazioneQbutton,corsi);
		navbar2.AddButtonAtActionBar(modificaQbutton);
		navbar2.AddButtonAtActionBar(eliminaQbutton);
		add(navbar2);
		hor.setHeight("100%");
		hor.setWidthFull();
		ConfigureGridQ();
		gridquiz.setSizeFull();
		add(quizForm);
		hor.add(gridquiz);
		add(hor);

	}


	private void UpdateGridQ() {
		if(corsi.getValue()==null){
			gridquiz.setItems(this.quizS.findAllByDocente(this.docente));
			}
			else
			{
				gridquiz.setItems(this.quizS.findAllByCorso(corsi.getValue()));
			}

		}


	private void ConfigureGridQ() {
		gridquiz.setColumns("id","nomeQuiz");
		gridquiz.setWidth("98%");
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
		gridquiz.addComponentColumn(item-> createValited(gridquiz,item)).setHeader("Attivato");
		gridquiz.asSingleSelect().addValueChangeListener(event->{
			this.quiz=event.getValue();
		});
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
