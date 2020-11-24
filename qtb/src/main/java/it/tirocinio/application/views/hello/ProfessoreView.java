package it.tirocinio.application.views.hello;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.view.form.CorsoForm;
import it.tirocinio.application.view.form.DomandaForm;
import it.tirocinio.application.view.form.QuizForm;
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

@Route(value = "professore", layout = MainView.class)
@PageTitle("ProfPage")
@CssImport("./styles/views/hello/hello-view.css")
public class ProfessoreView extends VerticalLayout{

	private String nome ;
	private Utente docente;
	private CorsoService corsoS;
	private UtenteService utenteS;
	private QuizService quizS;
	private DomandaService domandaS;
	private RispostaService rispostaS;
	Grid<Corso> gridtenuti = new Grid<>(Corso.class);
	Grid<Quiz> gridQuiz=new Grid<>(Quiz.class);
	Grid<Domanda> gridDomande=new Grid<>(Domanda.class);
	private Corso corso;

	private QuizForm quizForm;
	private DomandaForm domandaForm;
	private Div div2 =new Div();
	private Div div3 =new Div();
	HorizontalLayout horiz= new HorizontalLayout();
	Button creazioneQbutton= new Button("vuoi aggiungere un quiz per questo corso?",e->this.quizForm.setVisible(true));
	Button creazioneDbutton = new Button("vuoi aggiungere una domanda?",e->this.domandaForm.setVisible(true));


	public ProfessoreView(CorsoService s, UtenteService u,QuizService q,DomandaService d,RispostaService r){
		this.domandaS=d;
		this.rispostaS=r;
		this.corsoS=s;
		this.utenteS=u;
		this.quizS=q;
		HorizontalLayout hor = new HorizontalLayout();
	
		Div div1 = new Div();
		setPadding(true);	
		hor.add("Benvenuto Professor: ");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ( principal instanceof UserDetails){
			this.nome = ((UserDetails)principal).getUsername();
		}
		docente=this.utenteS.findByName(nome);
		CorsoForm corsoForm = new CorsoForm(this.corsoS,this.utenteS,docente);		
		gridQuiz.setVisible(false);
		gridDomande.setVisible(false);
		corsoForm.setVisible(false);
		
		hor.add(nome);
		add(hor);
		configureGridCorsi();
		div1.add(gridtenuti);
		updateGridCorsi();
		Button creazioneCbutton = new Button("vuoi aggiungere un corso?",e->corsoForm.setVisible(true));
		
		div1.add(creazioneCbutton,corsoForm);
		
		horiz.add(div1);
		add(horiz);

	}


	private void updateGridCorsi() {
		gridtenuti.setItems(this.corsoS.findbyDocente(nome));

	}


	private void configureGridCorsi() {
		gridtenuti.removeColumnByKey("id");
		gridtenuti.removeColumnByKey("utentifreq");
		gridtenuti.removeColumnByKey("docente");
		gridtenuti.removeColumnByKey("quizDelcorso");
		gridtenuti.setColumns("nomeCorso");		
		gridtenuti.setWidth("98%");
		gridtenuti.asSingleSelect().addValueChangeListener(event->updateGridQuiz(event.getValue()));
	}




	private void updateGridQuiz(Corso c) {
		if(c==null){
			return;
		}
		else
		{
			gridDomande.setVisible(false);
			creazioneDbutton.setVisible(false);
			gridQuiz.setItems(this.quizS.findAllByCorso(c));
		
			gridQuiz.setColumns("nomeQuiz");
			gridQuiz.setWidth("98%");
			gridQuiz.addComponentColumn(item-> createValited(gridQuiz,item)).setHeader("attivato");
			gridQuiz.asSingleSelect().addValueChangeListener(event->updateGridDomande(event.getValue()));
			gridQuiz.setVisible(true);
			if(this.quizForm==null){
				
			}
			else 
				this.quizForm.setVisible(false);
			this.quizForm=new QuizForm(this.quizS,c);
			
			this.quizForm.setVisible(false);
			div2.add(gridQuiz,creazioneQbutton,quizForm);
			horiz.add(div2);
		}
	}

	private void updateGridDomande(Quiz q) {
		// TODO Auto-generated method stub
		if(q==null){
			return;
		}
		else
		{
			creazioneDbutton.setVisible(true);
			gridDomande.setItems(this.domandaS.findByQuiz(q));
			gridDomande.setColumns("descrizione");
			gridDomande.setWidth("98%");
			gridDomande.setVisible(true);
					if(this.domandaForm==null){
				
			}
			else 
				this.domandaForm.setVisible(false);
			this.domandaForm=new DomandaForm(this.domandaS,this.rispostaS,q);
			
			this.domandaForm.setVisible(false);
			div3.add(gridDomande,creazioneDbutton,domandaForm);
			horiz.add(div3);
		}
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