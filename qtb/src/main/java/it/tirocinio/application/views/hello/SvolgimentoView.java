package it.tirocinio.application.views.hello;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.quiz.Quiz;

@Route(value = "svolgimento", layout = MainView.class)
@PageTitle("svolgimento Test")
@CssImport("./styles/views/hello/hello-view.css")
public class SvolgimentoView extends VerticalLayout implements HasUrlParameter<String> {
	private String para;
	private UtenteService utenteS ;
	private CorsoService corsoS;
	private QuizService quizS;
	private Quiz quiz;


	public SvolgimentoView(UtenteService u,CorsoService c,QuizService q){
		this.utenteS=u;
		this.quizS=q;
		this.corsoS=c;
	
	}
	


	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		this.para=parameter;
		vedi();
	}



	private void vedi() {
		this.quiz=this.quizS.findById(this.para);
		if(!(this.quiz==null)){
			add(this.quiz.getNomeQuiz());
		}
		else
			add("id non appartiente ad un quiz");
		
	}

}
