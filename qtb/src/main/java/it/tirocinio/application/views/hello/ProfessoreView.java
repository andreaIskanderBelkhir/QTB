package it.tirocinio.application.views.hello;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.view.form.CorsoForm;
import it.tirocinio.application.views.main.ActionBar;
import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.DomandaService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.RispostaService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;

@Route(value = "professore", layout = MainView.class)
@PageTitle("Gestione dei Corsi")
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
	private Corso corso;
	HorizontalLayout horiz= new HorizontalLayout();

	public ProfessoreView(CorsoService s, UtenteService u,QuizService q,DomandaService d,RispostaService r){
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
		this.docente=this.utenteS.findByName(nome);
		CorsoForm corsoForm = new CorsoForm(this.corsoS,this.utenteS,docente);		
		Button eliminaCbutton= new Button("Elimina",e->corsoForm.Elimina(gridtenuti, corso));
		Button creazioneCbutton = new Button("Nuovo",e->corsoForm.Nuovo(gridtenuti));
		Button modificaCbutton=new Button("Modifica",e->corsoForm.Modifica(gridtenuti,this.corso));
		Button selezioneCbutton =new Button("Selezione",e->corsoForm.Setselezione(gridtenuti,this.corso));
		TextField filter = new TextField();
		configureFilter(filter);
		ActionBar navbar=new ActionBar(selezioneCbutton,filter);
		navbar.AddButtonAtActionBar(creazioneCbutton);
		navbar.AddButtonAtActionBar(modificaCbutton);
		navbar.AddButtonAtActionBar(eliminaCbutton);
		add(navbar);
		if(docente.getRuolo().equals("PROFESSORE")){
			configureGridCorsiDocente();    
			updateGridCorsiDocente(filter);
		}
		else
		{
			configureGridCorsiAdmin();
			updateGridCorsiAdmin(filter);
		}
		gridtenuti.setVisible(true);
		horiz.setSizeFull();
		horiz.add(gridtenuti);
		add(horiz);
		add(corsoForm);


	}


	private void configureFilter(TextField filter) {
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e->{
			if(this.docente.getRuolo().equals("ADMIN")){
				updateGridCorsiAdmin(filter);
			}
			else{
				updateGridCorsiDocente(filter);
			}


		});

	}


	private void updateGridCorsiAdmin(TextField filter) {
		gridtenuti.setItems(this.corsoS.findbyDocente(docente,filter));
	}


	private void configureGridCorsiAdmin() {
		gridtenuti.setSizeFull();
		gridtenuti.removeColumnByKey("utentifreq");
		gridtenuti.removeColumnByKey("docente");
		gridtenuti.removeColumnByKey("quizDelcorso");
		gridtenuti.setColumns("id","nomeCorso","descrizioneCorso");	
		gridtenuti.addColumn(corso->{
			Utente docente= corso.getDocente();
			return docente==null ? "-": docente.getNome();
		}).setHeader("Docente del corso");
		gridtenuti.getColumns().forEach(c->c.setAutoWidth(true));
		gridtenuti.addColumn(corso->{
			return corso.getSelezione() ? "SI": "";
		}).setHeader("usato come selezione");
		gridtenuti.asSingleSelect().addValueChangeListener(event->{
			this.corso=event.getValue();
		});


	}


	private void updateGridCorsiDocente(TextField filter) {
		gridtenuti.setItems(this.corsoS.findbyDocente(docente,filter));

	}


	private void configureGridCorsiDocente() {
		gridtenuti.setSizeFull();

		gridtenuti.removeColumnByKey("utentifreq");
		gridtenuti.removeColumnByKey("docente");
		gridtenuti.removeColumnByKey("quizDelcorso");
		gridtenuti.setColumns("id","nomeCorso","descrizioneCorso");		
		gridtenuti.getColumns().forEach(c->c.setAutoWidth(true));
		gridtenuti.asSingleSelect().addValueChangeListener(event->{
			this.corso=event.getValue();
		});

	}


}