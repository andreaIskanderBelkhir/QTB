package it.tirocinio.application.views.hello;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.view.form.CorsoForm;
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
		HorizontalLayout hor = new HorizontalLayout();
		setSizeFull();
		hor.setId("prof-navbar");
		H3 h=new H3("");
        hor.add(h);
		hor.expand(h);
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ( principal instanceof UserDetails){
			this.nome = ((UserDetails)principal).getUsername();
		}	
		docente=this.utenteS.findByName(nome);
		CorsoForm corsoForm = new CorsoForm(this.corsoS,this.utenteS,docente);		
		corsoForm.setVisible(false);
		
		horiz.setSizeFull();
		horiz.setSpacing(true);
		Button creazioneCbutton = new Button("Nuovo",e->corsoForm.setVisible(true));
		creazioneCbutton.setIcon(new Icon(VaadinIcon.PLUS));
		creazioneCbutton.setIconAfterText(true);
		creazioneCbutton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		hor.add(creazioneCbutton);
		add(hor);
		if(docente.getRuolo().equals("PROFESSORE")){
		configureGridCorsiDocente();    
		updateGridCorsiDocente();
		}
		else
		{
			configureGridCorsiAdmin();
			updateGridCorsiAdmin();
		}

		horiz.add(gridtenuti,corsoForm);
		add(horiz);
		

	}


	private void updateGridCorsiAdmin() {
		gridtenuti.setItems(this.corsoS.findAll());
		
	}


	private void configureGridCorsiAdmin() {
		gridtenuti.setSizeFull();
		gridtenuti.removeColumnByKey("id");
		gridtenuti.removeColumnByKey("utentifreq");
		gridtenuti.removeColumnByKey("docente");
		gridtenuti.removeColumnByKey("quizDelcorso");
		gridtenuti.setColumns("nomeCorso","descrizioneCorso");	
		gridtenuti.addColumn(corso->{
			Utente docente= corso.getDocente();
			return docente==null ? "-": docente.getNome();
		}).setHeader("Docente del corso");
		gridtenuti.getColumns().forEach(c->c.setAutoWidth(true));
		
		
	}


	private void updateGridCorsiDocente() {
		gridtenuti.setItems(this.corsoS.findbyDocente(nome));

	}


	private void configureGridCorsiDocente() {
		gridtenuti.setSizeFull();
		gridtenuti.removeColumnByKey("id");
		gridtenuti.removeColumnByKey("utentifreq");
		gridtenuti.removeColumnByKey("docente");
		gridtenuti.removeColumnByKey("quizDelcorso");
		gridtenuti.setColumns("nomeCorso","descrizioneCorso");		
		gridtenuti.getColumns().forEach(c->c.setAutoWidth(true));
		

	}


}