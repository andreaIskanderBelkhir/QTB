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

import it.tirocinio.application.views.login.CorsoForm;
import it.tirocinio.application.views.login.QuizForm;
import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;

import it.tirocinio.entity.quiz.Corso;
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
	Grid<Corso> gridtenuti = new Grid<>(Corso.class);
	Grid<Quiz> gridQuiz=new Grid<>(Quiz.class);
	private Corso corso;

	private QuizForm quizForm;
	private Div div2 =new Div();
	HorizontalLayout horiz= new HorizontalLayout();
	Button creazioneQbutton= new Button("vuoi aggiungere un quiz per questo corso?",e->this.quizForm.setVisible(true));


	public ProfessoreView(CorsoService s, UtenteService u,QuizService q){
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
		CorsoForm corsoForm = new CorsoForm(this.corsoS,docente);		
		gridQuiz.setVisible(false);
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
			
			gridQuiz.setItems(this.quizS.findAllByCorso(c));
		
			gridQuiz.setColumns("nomeQuiz");
			gridQuiz.addColumn(quiz-> {
				Corso corso=quiz.getCorsoAppertenenza();
				if(corso==null)
				{
					return"";
				}
				else{
					return corso.getNomeCorso();}
			}).setHeader("corsoApparteneza");
			gridQuiz.setWidth("98%");
			gridQuiz.addComponentColumn(item-> createValited(gridQuiz,item)).setHeader("attivato");
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