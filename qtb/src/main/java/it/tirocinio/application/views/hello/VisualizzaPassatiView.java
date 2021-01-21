package it.tirocinio.application.views.hello;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.views.main.ActionBar;
import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Quiz;

@Route(value = "superati", layout = MainView.class)
@PageTitle("Visualizza Studenti che hanno passato il test selezionato")
public class VisualizzaPassatiView extends VerticalLayout{
	private String nome;
	private Utente docente;
	private UtenteService utenteS ;
	private QuizService quizS;
	ComboBox<Quiz> quizs ;
	Grid<Utente> gridutente = new Grid<>(Utente.class);
	
	
	
	public VisualizzaPassatiView(UtenteService u,QuizService q){
		this.quizS=q;
		this.utenteS=u;
		 Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    if ( principal instanceof UserDetails){
			  this.nome = ((UserDetails)principal).getUsername();
	    	}
		    docente = this.utenteS.findByName(nome);
		    Button b=new Button();
		    ConfigureGridU();
		    quizs=new ComboBox<>();
			quizs.setItemLabelGenerator(Quiz::getNomeQuiz);
			quizs.setItems(this.quizS.findAllByDocente(docente));
			quizs.addValueChangeListener(e->{
				
				UpdateGridU(e.getValue());
				
			});
			b.setVisible(false);
			ActionBar navbar2=new ActionBar(b,quizs,1);
			add(navbar2);
			
			add(gridutente);
	}



	private void UpdateGridU(Quiz quiz) {
		List<Utente> sup=this.utenteS.findByCorso(quiz.getCorsoAppartenenza());
		List<Utente> superati=new ArrayList<Utente>();
		for(Utente u:sup){
			if(u.getQuizpassati().contains(quiz.getID())){
				superati.add(u);
			}
		}
		gridutente.setItems(superati);
	}
	
	private void ConfigureGridU(){
		
		gridutente.setColumns("ID");
		gridutente.addColumn(utente->{
			return utente.getNome();
		}).setHeader("Nome");
		
	}



	private Checkbox createCheck(Grid<Utente> gridutente2, Utente item,Quiz quiz) {
		Checkbox check = new Checkbox();
		if(item.getQuizpassati().contains(quiz.getID())){
			check.setValue(true);
		}
		else
			check.setValue(false);
		check.setEnabled(false);
		return check;
		
	}
}
