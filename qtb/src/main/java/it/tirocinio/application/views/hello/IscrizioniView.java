package it.tirocinio.application.views.hello;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.view.form.IscriviForm;
import it.tirocinio.application.views.main.ActionBar;
import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;

@Route(value = "iscrizioni", layout = MainView.class)
@PageTitle("Gestione delle iscrizioni")
@CssImport("./styles/views/hello/hello-view.css")
public class IscrizioniView extends VerticalLayout {
	private String nome ;
	private Utente docente;
	private CorsoService corsoS;
	private UtenteService utenteS;
	Grid<Corso> gridcorsi = new Grid<>(Corso.class);
	Grid<Utente> gridUtenti = new Grid<>(Utente.class);
	private Corso corso;
	IscriviForm iscriviform;
	
	
	public IscrizioniView(CorsoService s, UtenteService u){
		this.corsoS=s;
		this.utenteS=u;
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ( principal instanceof UserDetails){
			this.nome = ((UserDetails)principal).getUsername();
		}	
		docente=this.utenteS.findByName(nome);
		 iscriviform=new IscriviForm(corsoS, utenteS);
		 
		Button creazioneIbutton = new Button("Iscrivi",e->iscriviform.Iscrivi(corso,gridUtenti));
		H3 h=new H3("");
		ActionBar navbar=new ActionBar(creazioneIbutton,h);
		add(navbar);
		setSizeFull();
		ConfigureGridCorsi();
		ConfigureGridUtenti();
		UpdateGridCorsi();
		HorizontalLayout hor2= new HorizontalLayout();
		hor2.setSizeFull();
		hor2.add(gridcorsi,gridUtenti);
		add(hor2,iscriviform);
	}


	private void UpdateGridCorsi() {
		List<Corso> co=new ArrayList<Corso>();
		for(Corso c:this.corsoS.findbyDocente(docente)){
			if(!(c.getSelezione()))
				co.add(c);
		}
		gridcorsi.setItems(co);		
	}


	private void ConfigureGridUtenti() {
		gridUtenti.setColumns("id");
		gridUtenti.addColumn(utente->{
			return utente.getNome();
		}).setHeader("Nome");
		gridUtenti.addColumn(u->{
			if(corso.getUtentirischiesta().contains(u.getId())){
				return "Da approvare";
			}
			return "";
		});
		
	}


	private void ConfigureGridCorsi() {
		gridcorsi.setColumns("nomeCorso");	
		gridcorsi.asSingleSelect().addValueChangeListener(event->{
			updateGridUtenti(event.getValue());
			this.corso=event.getValue();
			});
		
	}


	private void updateGridUtenti(Corso value) {
		List<Utente> utenti= this.utenteS.findByCorso(value);
		utenti.addAll(this.corsoS.getStudenteRichiesta(value.getUtentirischiesta()));
		gridUtenti.setItems(utenti);
	}
}
