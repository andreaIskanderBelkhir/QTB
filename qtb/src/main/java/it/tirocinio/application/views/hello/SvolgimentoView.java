package it.tirocinio.application.views.hello;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.jsoup.select.Evaluator.IsEmpty;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.flowingcode.vaadin.addons.simpletimer.SimpleTimer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.tirocinio.application.views.main.ActionBar;
import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.CorsoService;
import it.tirocinio.backend.service.QuizService;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;
import it.tirocinio.entity.quiz.Risposta;

@Route(value = "svolgimento")
@PageTitle("svolgimento Test")
@CssImport("./styles/views/hello/hello-view.css")
public class SvolgimentoView extends VerticalLayout implements HasUrlParameter<String> {
	private String para;
	private UtenteService utenteS ;
	private CorsoService corsoS;
	private QuizService quizS;
	private Quiz quiz;
	private Button precedente;
	private Button successivo;
	private Button consegna;
	private List<Domanda> domande;
	private List<Set<Risposta>> rispostedate;
	private int index=0; 
	private int numero;
	private H1 testo;
	SimpleTimer timer;
	boolean sbagliato;
	int giusten;
	private CheckboxGroup<Risposta> radioGroup;
	ActionBar navbar;
	private int vero;
	private H3 h;
	private String nome;
	private Utente studente;

	public SvolgimentoView(UtenteService u,CorsoService c,QuizService q){
		this.utenteS=u;
		this.quizS=q;
		this.corsoS=c;
		this.precedente=new Button("precedente");
		this.successivo=new Button("successivo");
		this.consegna=new Button("Consegna");

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ( principal instanceof UserDetails){
			this.nome = ((UserDetails)principal).getUsername();
		}
		studente = this.utenteS.findByName(nome);



	}



	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		this.para=parameter;
		vedi();

		this.h=new H3("("+quiz.getNomeQuiz()+") Domanda °");
		if(!(this.quiz==null)){
			domande=new ArrayList<Domanda>();
			domande.addAll(this.quiz.getDomande());
			rispostedate=new ArrayList<Set<Risposta>>();
			for(int i=0;i<domande.size();i++){
				rispostedate.add(new HashSet<Risposta>());
			}
			if(!(domande.isEmpty())){
				String tempo=String.valueOf(quiz.getTempo()*60);
				timer = new SimpleTimer(new BigDecimal(tempo));
				timer.start();
				timer.addTimerEndEvent(e->{
					Dialog dialog= new Dialog();
					Button consegna=new Button("Consegna");
					Button annulla=new Button("annulla compito");
					HorizontalLayout hor=new HorizontalLayout();
					hor.add(consegna,annulla);
					dialog.add(new H2("Tempo scaduto"),hor);
					add(dialog);
					dialog.open();
					annulla.addClickListener(m-> dialog.close());
					consegna.addClickListener(m->{
						dialog.close();
						navbar.updateNdomanda(0);
						consegnaTest();
					});
				});
				this.navbar=new ActionBar(precedente,h,timer);
				navbar.AddButtonAtActionBar(successivo);
				navbar.AddButtonAtActionBar(consegna);
				precedente.setIcon(new Icon(VaadinIcon.ARROW_CIRCLE_LEFT_O));
				successivo.setIcon(new Icon(VaadinIcon.ARROW_CIRCLE_RIGHT_O));
				consegna.setIcon(new Icon(VaadinIcon.ARROW_CIRCLE_UP_O));
				add(navbar);
				updateDomanda(index);
				settaPulsanti();

			}

		}
	}





	private void settaPulsanti() {
		if(index==0){
			precedente.setEnabled(false);
		}
		if(domande.size()==1){
			successivo.setEnabled(false);
		}

		precedente.addClickListener(e->{
			successivo.setEnabled(true);
			index=index-1;
			updateDomanda(index);
			if(index==0)
				precedente.setEnabled(false);
		});
		successivo.addClickListener(e->{
			precedente.setEnabled(true);
			index=index+1;
			updateDomanda(index);
			if(index==(domande.size()-1))
				successivo.setEnabled(false);

		});
		consegna.addClickListener(e->{
			List listdo=new ArrayList<>();
			for(int i=0;i<this.domande.size();i++){
				if(rispostedate.get(i).isEmpty()){
					listdo.add(i);
				}
			}
			if(!(listdo.isEmpty())){
				Dialog dialog2=new Dialog();
				Button consegna2=new Button("Avanti");
				Button annulla2=new Button("Torna indietro");
				HorizontalLayout hor2=new HorizontalLayout();
				H3 h0=new H3("Alcune domande non hanno risposta vuoi andare avanti con la consegna?");
				dialog2.add(h0);
				for(int i=0;i<listdo.size();i++){
					H5 h00=new H5("la domanda "+(i+1)+" non ha risposta");
					dialog2.add(h00);
				}
				dialog2.add(hor2);
				H5 h5=new H5("");
				hor2.setAlignItems(Alignment.END);
				hor2.add(h5,consegna2,annulla2);
				hor2.expand(h5);
				add(dialog2);
				dialog2.open();
				annulla2.addClickListener(m-> dialog2.close());
				consegna2.addClickListener(m->{
					dialog2.close();
					Dialog dialog= new Dialog();
					dialog.setWidth("500px");
					dialog.setHeight("200px");
					Button consegna=new Button("Consegna");
					Button annulla=new Button("Torna indietro");
					HorizontalLayout hor=new HorizontalLayout();
					H3 h01=new H3("Sei sicuro di voler consegnare ?");
					dialog.add(h01,hor);
					H5 h6=new H5("");
					hor.getStyle().set("margin-top", "50px");
					hor.setAlignItems(Alignment.END);
					hor.add(h6,consegna,annulla);
					hor.expand(h6);
					dialog.open();
					annulla.addClickListener(l-> dialog.close());
					consegna.addClickListener(l->{
						dialog.close();
						navbar.updateNdomanda(0);
						consegnaTest();
					});
				});		
			}
			
		});
	}



	private void consegnaTest() {
		if((!(testo==null))&&(!(radioGroup==null))){
			timer.pause();
			remove(testo,radioGroup);
			H2 testo1=new H2("Il test è stato Consegnato");
			Icon icon =new Icon(VaadinIcon.CHECK_CIRCLE_O);
			icon.getStyle().set("color","#1fe805");
			testo1.add(icon);
			add(testo1);
			this.precedente.setEnabled(false);
			this.successivo.setEnabled(false);
			this.consegna.setEnabled(false);
			contaRisposte();
		}

	}



	private void contaRisposte() {
		int numerototale=this.domande.size();
		String numerototaleString=String.valueOf(numerototale);
		int numerogiusto=0;
		int numerosbagliate=0;
		int numeronondate=0;
		for(int i=0;i<numerototale;i++){
			giusten=0;
			vero=0;
			sbagliato=false;
			for(Risposta re:domande.get(i).getRisposte()){
				if(re.getGiusta().booleanValue())
					giusten++;
			}
			if(!(rispostedate.get(i).isEmpty())){
				for(Risposta r:rispostedate.get(i)){
					if(r.getGiusta().booleanValue()){
						vero++;
					}
					else
						sbagliato=true;						
				}
				if((vero==giusten)&&(!sbagliato)){
					numerogiusto++;
				}
				else
					numerosbagliate++;
			}
			else
				numeronondate++;
		}
		String numerogiusteString=String.valueOf(numerogiusto);
		String numerosbagliateString=String.valueOf(numerosbagliate);
		String numeronondateString=String.valueOf(numeronondate);
		H3 giuste=new H3("N° Risposte giuste : ");
		giuste.add(numerogiusteString);
		giuste.add("/");
		giuste.add(numerototaleString);
		H3 sbagliate=new H3("N° Risposte sbagliate : ");
		sbagliate.add(numerosbagliateString);
		sbagliate.add("/");
		sbagliate.add(numerototaleString);
		H3 nondate=new H3("N° Risposte non date : ");
		nondate.add(numeronondateString);
		nondate.add("/");
		nondate.add(numerototaleString);
		VerticalLayout ver = new VerticalLayout();
		ver.add(giuste,sbagliate,nondate);
		if(quiz.getModalitaPercentuale()){
			conteggioPercentuale(ver,numerogiusto,numerototale);
		}
		else
			conteggioDomande(ver,numerogiusto,numerosbagliate,numerototale);
		add(ver);
	}

	private void conteggioPercentuale(VerticalLayout ver, int numerogiusto, int numerototale) {
		double per=((double)numerogiusto/(double)numerototale)*100;
		H3 perct=new H3("la tua percentuale di accuratezza è : ");
		perct.add(String.valueOf(per));
		perct.add("%");
		HorizontalLayout passagio=new HorizontalLayout();
		passagio.setWidthFull();
		passagio.setAlignItems(Alignment.CENTER);
		H2 risultato=new H2();
		H2 risultato2=new H2("");
		if(per>=quiz.getSogliaPercentuale()){
			risultato.add("PASSATO");
			risultato.getStyle().set("color", "#19bf0a");
			risultato.getStyle().set("font-weight", "900");
			passagio.add(risultato,risultato2);
			ver.add(perct,passagio);
			this.utenteS.addQuizpassati(studente,quiz.getID());
			if(studente.getValoretesteffetuati().get(quiz.getID())==null){
				this.utenteS.addQuizvalore(studente, quiz.getID(),per);

			}
			else{
				if(studente.getValoretesteffetuati().get(quiz.getID())<per){
					this.utenteS.addQuizvalore(studente,quiz.getID(),per);

				}
			}
		}
		else
		{
			risultato.add("NON");
			risultato.getStyle().set("color", "#f50509");
			risultato2.add(" Passato");
			risultato.getStyle().set("font-weight", "900");
			H3 dapassare=new H3("dovevi totalizzare almeno : ");
			dapassare.add(String.valueOf(quiz.getSogliaPercentuale()));
			dapassare.add("%");
			passagio.add(risultato,risultato2);
			ver.add(perct,passagio,dapassare);
			if(studente.getValoretesteffetuati().get(quiz.getID())==null){
				this.utenteS.addQuizvalore(studente, quiz.getID(),per);

			}
			else{
				if(studente.getValoretesteffetuati().get(quiz.getID())<per){
					this.utenteS.addQuizvalore(studente,quiz.getID(),per);

				}
			}

		}
		passagio.setVerticalComponentAlignment(Alignment.CENTER,risultato2);
		risultato.getElement().getStyle().set("margin-left", "40%");

	}



	private void conteggioDomande(VerticalLayout ver, int numerogiusto, int numerosbagliate, int numerototale) {
		double valoreg=numerogiusto*quiz.getValoreGiusta();
		double valores=numerosbagliate*quiz.getValoreSbagliata();
		double valoretotale=numerototale*quiz.getValoreGiusta();
		H3 totale =new H3();
		totale.add(String.valueOf(valoreg-valores));
		totale.add("/");
		totale.add(String.valueOf(valoretotale));
		HorizontalLayout passagio=new HorizontalLayout();
		ver.add(totale);
		passagio.setWidthFull();
		passagio.setAlignItems(Alignment.CENTER);
		H2 risultato=new H2();
		H2 risultato2=new H2("");
		if((valoreg-valores)>=quiz.getSoglia()){

			risultato.add("PASSATO");
			risultato.getStyle().set("color", "#19bf0a");
			risultato.getStyle().set("font-weight", "900");
			passagio.add(risultato,risultato2);
			ver.add(passagio);
			this.utenteS.addQuizpassati(studente,quiz.getID());
			if(studente.getValoretesteffetuati().get(quiz.getID())==null){
				this.utenteS.addQuizvalore(studente, quiz.getID(),(valoreg-valores));

			}
			else{
				if(studente.getValoretesteffetuati().get(quiz.getID())<(valoreg-valores)){
					this.utenteS.addQuizvalore(studente,quiz.getID(),(valoreg-valores));

				}
			}
		}
		else
		{
			risultato.add("NON");
			risultato.getStyle().set("color", "#f50509");
			risultato2.add(" Passato");
			risultato.getStyle().set("font-weight", "900");
			H3 dapassare=new H3("dovevi totalizzare almeno : ");
			dapassare.add(String.valueOf(quiz.getSoglia()));
			passagio.add(risultato,risultato2);
			ver.add(passagio,dapassare);
			if(studente.getValoretesteffetuati().get(quiz.getID())==null){
				this.utenteS.addQuizvalore(studente, quiz.getID(),(valoreg-valores));

			}
			else{
				if(studente.getValoretesteffetuati().get(quiz.getID())<(valoreg-valores)){
					this.utenteS.addQuizvalore(studente,quiz.getID(),(valoreg-valores));

				}
			}

		}
		passagio.setVerticalComponentAlignment(Alignment.CENTER,risultato2);
		risultato.getElement().getStyle().set("margin-left", "40%");


	}



	private void updateDomanda(int i) {
		if((testo==null)){

		}
		else{
			remove(testo);
		}
		this.testo=new H1(domande.get(i).getDescrizionedomanda());
		this.numero=i+1;
		navbar.updateNdomanda(numero);
		add(testo);
		setAlignItems(Alignment.START);
		creazioneRisposte(i);
	}


	private void creazioneRisposte(int i) {
		Domanda domanda=domande.get(i);
		List<Risposta> risposte = new ArrayList<>();
		risposte.addAll(domanda.getRisposte());
		if(domanda.getRandomordine()){
			Collections.shuffle(risposte);
		}
		if(!(risposte.isEmpty())){
			if(!(radioGroup==null)){
				remove(radioGroup);
			}
			radioGroup = new CheckboxGroup<>();
			radioGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
			radioGroup.setItems(risposte);		
			radioGroup.setItemLabelGenerator(Risposta::getRisposta);
			if(!(rispostedate.get(i).isEmpty())){
				radioGroup.setValue(rispostedate.get(i));
			}
			radioGroup.addValueChangeListener(eve -> {		
				if(!(eve.getValue()==null)){		
					for(Risposta r:radioGroup.getSelectedItems()){		
						rispostedate.get(i).add(r);
					}
				}
				if(radioGroup.getSelectedItems().isEmpty()){
					rispostedate.get(i).clear();
				}
			});
			add(radioGroup);
		}

	}



	private void vedi() {
		this.quiz=this.quizS.findById(this.para);
		if(!(this.quiz==null)){

		}
		else
			add("id non appartiente ad un quiz");

	}

}
