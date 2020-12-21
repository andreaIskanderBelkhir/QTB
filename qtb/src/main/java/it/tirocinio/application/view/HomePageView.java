package it.tirocinio.application.view;



import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;


import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;

@Route(value="Homepage",layout=MainView.class)
@PageTitle("Homepage")
@RouteAlias(value = "", layout = MainView.class)
public class HomePageView extends VerticalLayout{
	private String nome ;
	private Utente docente;
	private UtenteService utenteS;


	public HomePageView(UtenteService u){
		this.utenteS=u;
		setAlignItems(FlexComponent.Alignment.START);
		add("sei sulla home   ");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ( principal instanceof UserDetails){
			this.nome = ((UserDetails)principal).getUsername();
		}
		else{
			this.nome = principal.toString();
		}
		this.docente=this.utenteS.findByName(nome);		
		add(nome);

		switch(docente.getRuolo().toString()){
		case "ADMIN":
			IstruzioniAdmin();
			break;
		case "PROFESSORE":
			IstruzioniProfessore();
			break;
		case "USER":
			IstruzioniStudente();
			break;
		case "CANDIDATO":
			IstruzioniCandidato();
			break;
		}

	}


	public void IstruzioniAdmin(){
		Div div =new Div();
		Div div2 =new Div();
		Div div3 =new Div();
		Div div4 =new Div();
		Div div5 =new Div();
		Div div6 =new Div();
		Div div7 =new Div();
		Div div8 =new Div();
		Div div9=new Div();
		div.add("Ecco cosa puoi fare :");
		div2.add( "I miei corsi: puoi fare richiesta di iscrizione ad un corso e svolgere i test");
		div3.add("Gestione Corsi: puoi creare corsi e nel caso renderli come selezione per la tua azienda");
		div4.add("Gestione Test: puoi creare i test per i corsi");
		div9.add("Gestione Domande: puoi creare domande e risposte per i test");
		div5.add("Gestione Iscrizioni : puoi iscrivere studenti ai tuoi corsi");
		div6.add("Visualizza Passati : puoi visualizzare i studenti che hanno passato i test");
		div7.add("Gestione Selezione: puoi creare candidati per un corso e vedere chi ha passato i test delle selezioni");
		div8.add("Admin: puoi vedere le richieste di iscrizione al sito");
		add(div,div2,div3,div4,div9,div5,div6,div7,div8);
	}

	public void IstruzioniProfessore(){
		Div div =new Div();
		Div div7 =new Div();
		Div div3 =new Div();
		Div div4 =new Div();
		Div div5 =new Div();
		Div div6 =new Div();
		Div div9=new Div();
		div.add("Ecco cosa puoi fare :");
		div3.add("Gestione Corsi: puoi creare corsi e nel caso renderli come selezione per la tua azienda");
		div4.add("Gestione Test: puoi creare i test per i corsi");
		div9.add("Gestione Domande: puoi creare domande e risposte per i test");
		div5.add("Gestione Iscrizioni: puoi iscrivere studenti ai tuoi corsi");
		div7.add("Visualizza Passati: puoi visualizzare i studenti che hanno passato i test");
		div6.add("Gestione Selezione: puoi creare candidati per un corso e vedere chi ha passato i test delle selezioni");
		add(div,div3,div4,div9,div5,div6,div7);	
	}
	public void IstruzioniStudente(){
		Div div =new Div();
		Div div2 =new Div();
		div.add("Ecco cosa puoi fare :");
		div2.add( "I miei corsi: puoi fare richiesta di iscrizione ad un corso e svolgere i test");
		add(div,div2);
	}
	public void IstruzioniCandidato(){
		Div div =new Div();
		Div div2 =new Div();
		div.add("Ecco cosa puoi fare :");
		div2.add( "Selezione: puoi effetuare le selezioni per l'azienda che ti ha dato le credenziali");
		add(div,div2);	

	}
}
