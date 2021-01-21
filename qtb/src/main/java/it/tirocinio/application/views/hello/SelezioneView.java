package it.tirocinio.application.views.hello;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.view.form.CandidatoForm;
import it.tirocinio.application.views.main.ActionBar;
import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Quiz;

@Route(value = "selezioni", layout = MainView.class)
@PageTitle("Selezioni")
public class SelezioneView extends VerticalLayout{
	private String nome ;
	private Utente docente;
	private CorsoService corsoS;
	private UtenteService utenteS;
	private QuizService quizS;
	Grid<Quiz> gridquiz = new Grid<>(Quiz.class);
	Grid<Utente> gridcandidati = new Grid<>(Utente.class);
	ComboBox<Corso> corsi;




	public SelezioneView(CorsoService s, UtenteService u,QuizService q){
		this.corsoS=s;
		this.utenteS=u;
		this.quizS=q;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ( principal instanceof UserDetails){
			this.nome = ((UserDetails)principal).getUsername();
		}	
		docente=this.utenteS.findByName(nome);
		if((docente.getRuolo().equals("ADMIN"))||(docente.getRuolo().equals("PROFESSORE"))){
			CandidatoForm candidatoform=new CandidatoForm(corsoS, utenteS);
			Button creazioneCandidatobutton = new Button("Candidato",e->candidatoform.Nuovo(corsi.getValue()));
			corsi=new ComboBox<>();
			corsi.setItemLabelGenerator(Corso::getNomeCorso);
			corsi.setItems(this.corsoS.findbySelezione());
			ConfigureGridQ();
			HorizontalLayout hor2= new HorizontalLayout();
			if(this.corsoS.findbySelezione().isEmpty()){
				corsi.setEnabled(false);
			}
			else
			{
				corsi.setPlaceholder("");
				corsi.setOpened(true);
			}
			corsi.addValueChangeListener(e->{
				UpdateGridQ();
			});
			ActionBar navbar2=new ActionBar(corsi,creazioneCandidatobutton);
			ConfigureGridC();		
			add(navbar2);
			hor2.setSizeFull();
			hor2.add(gridquiz,gridcandidati);		
			add(hor2,candidatoform);

		}
		else
		{
			ConfigureGridQCandidato();
			UpdateGridQCandidato();
			setSizeFull();
			add(gridquiz);
		}
	}



	private void ConfigureGridC() {
		gridcandidati.setColumns();
		gridcandidati.addColumn(e->{
			return e.getNome();
		}).setHeader("Candidati che hanno superato");
	}



	private void UpdateGridQCandidato() {
		gridquiz.setItems(this.quizS.findAllByCorso(docente.getCorsoSelezione()));
	}



	//per ora prende tutti i test dei corsi settati come selezione
	private void UpdateGridQ() {
		if(corsi.getValue()!=null)
			gridquiz.setItems(this.quizS.findAllbySelezioneandAttivati(corsi.getValue()));

	}




	private void ConfigureGridQ() {
		gridquiz.setColumns();
		gridquiz.addColumn(e->{	
			return e.getNomeQuiz();
		}).setHeader("Nome test");
		gridquiz.asSingleSelect().addValueChangeListener(event->updateGridCandidati(event.getValue()));

	}

	private void updateGridCandidati(Quiz quiz) {
		if(quiz==null){
			return;
		}
		else
		{
			List<Utente> sup=this.utenteS.findByCorsoSelezione(quiz.getCorsoAppartenenza());
			List<Utente> superati=new ArrayList<Utente>();
			for(Utente u:sup){
				if(u.getQuizpassati().contains(quiz.getID())){
					superati.add(u);
				}
			}
			gridcandidati.setItems(superati);
		}
	}



	private void ConfigureGridQCandidato() {
		gridquiz.setColumns();
		gridquiz.addColumn(e->{	
			return e.getNomeQuiz();
		}).setHeader("Nome test");
		gridquiz.addComponentColumn(e->createLink(gridquiz,e));

	}
	private Anchor createLink(Grid<Quiz> gridQuiz2, Quiz item) {
		String a=new String("svolgimento/"+ item.getID().toString());
		Anchor link=new Anchor(a);
		Button button = new Button("Svolgi",new Icon(VaadinIcon.ARROW_FORWARD));
		button.setIconAfterText(true);	
		link.add(button);
		if(docente.getQuizpassati().contains(item.getID())){
			button.setVisible(false);
		}	
		return link;
	}
}
