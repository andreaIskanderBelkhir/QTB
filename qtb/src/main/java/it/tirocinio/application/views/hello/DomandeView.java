package it.tirocinio.application.views.hello;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.view.form.DomandaForm;
import it.tirocinio.application.view.form.RispostaForm;
import it.tirocinio.application.views.main.ActionBar;
import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.DomandaService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.RispostaService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;
import it.tirocinio.entity.quiz.Risposta;

@Route(value = "domandeelenco", layout = MainView.class)
@PageTitle("Gestione delle domande")
@CssImport("./styles/views/hello/hello-view.css")
public class DomandeView extends VerticalLayout {
	private Risposta risposta;
	private String nome ;
	private Utente docente;
	private CorsoService corsoS;
	private UtenteService utenteS;
	private QuizService quizS;
	private DomandaService domandaS;
	private RispostaService rispostaS;
	Domanda domanda;
	HorizontalLayout hor=new HorizontalLayout();
	Grid<Domanda> griddomanda= new Grid<>(Domanda.class);
	Grid<Risposta> gridrisposta= new Grid<>(Risposta.class);
	ComboBox<Quiz> quizs ;
	
	public DomandeView(CorsoService s, UtenteService u,QuizService q,DomandaService d,RispostaService r){

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
		
		quizs=new ComboBox<>();
		quizs.setItemLabelGenerator(Quiz::getNomeQuiz);
		quizs.setItems(this.quizS.findAllByDocente(docente));
		quizs.addValueChangeListener(e->{
			UpdateGridD();
		});
		DomandaForm domandaForm=new DomandaForm(domandaS, corsoS, quizS, docente);
		Button creazioneDbutton = new Button("Nuovo",e->domandaForm.Nuovo(quizs.getValue(),griddomanda));
		Button modificaDbutton = new Button("Modifica",e->domandaForm.Modifica(griddomanda,this.domanda));
		Button eliminaDbutton = new Button("Elimina",e->domandaForm.Elimina(quizs.getValue(),this.domanda,griddomanda));
		ActionBar navbar2=new ActionBar(creazioneDbutton,quizs,1);
		navbar2.AddButtonAtActionBar(modificaDbutton);
		navbar2.AddButtonAtActionBar(eliminaDbutton);
		add(navbar2);
		hor.setHeight("100%");
		hor.setWidthFull();
		ConfigureGridD();
		griddomanda.setSizeFull();
		add(domandaForm);
		Div div1=new Div();
		div1.setSizeFull();
		div1.add(griddomanda);
		Div div2 =new Div();
		div2.setSizeFull();
		RispostaForm rispostaForm = new RispostaForm(quizS, corsoS, domandaS, rispostaS, utenteS);
		Button creazioneRbutton = new Button("Nuovo",e->rispostaForm.Nuovo(quizs.getValue(),this.domanda,gridrisposta));
		Button modificaRbutton = new Button("Modifica",e->rispostaForm.Modifica(quizs.getValue(),this.risposta,gridrisposta));
		Button eliminaRbutton = new Button("Elimina",e->rispostaForm.Elimina(gridrisposta,this.risposta));
		H3 h3=new H3("Risposte : ");
		ActionBar navbarRisposte=new ActionBar(creazioneRbutton,h3);
		navbarRisposte.AddButtonAtActionBar(modificaRbutton);
		navbarRisposte.AddButtonAtActionBar(eliminaRbutton);
		ConfigureGridR();
		div2.add(navbarRisposte);
		div2.add(gridrisposta);
		add(rispostaForm);
		hor.add(div1,div2);
		add(hor);
	}

	private void ConfigureGridR() {
		gridrisposta.setColumns("ID","risposta");
		gridrisposta.addComponentColumn(item-> createCheck(gridrisposta,item)).setHeader("Corretta");	
		gridrisposta.asSingleSelect().addValueChangeListener(event->{
			this.risposta=event.getValue();	
		});
	}

	private Checkbox createCheck(Grid<Risposta> gridrisposta2, Risposta item) {
		Checkbox check = new Checkbox();
		check.setValue(item.getGiusta());
		check.setEnabled(false);
		return check;
	}

	private void UpdateGridD() {
		
			if(quizs.getValue()==null){
			}
			else
			{
				griddomanda.setItems(this.domandaS.findByQuiz(quizs.getValue()));
			}	
	}
	private void ConfigureGridD() {
		griddomanda.setColumns("ID");
		griddomanda.addColumn(domanda->{
			return domanda.getNomedomanda();
		}).setHeader("Nome");
		griddomanda.addColumn(domanda->{
			return domanda.getDescrizionedomanda();
		}).setHeader("Testo");
		griddomanda.setWidth("98%");
		griddomanda.asSingleSelect().addValueChangeListener(event->{
		this.domanda=event.getValue();	
		updateGridR(event.getValue());
		});
	}

	private void updateGridR(Domanda value) {
		if(value==null){
			
		}
		else{
			gridrisposta.setItems(this.domandaS.findRisposte(value));
		}
	}
}
