package it.tirocinio.application.views.hello;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import com.flowingcode.vaadin.addons.simpletimer.SimpleTimer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
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
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;
import it.tirocinio.entity.quiz.Risposta;

@Route(value = "svolgimento", layout = MainView.class)
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
	private Risposta[] rispostedate;
	private int index=0; 
	private int numero;
	private H1 testo;
	SimpleTimer timer;
	private RadioButtonGroup<Risposta> radioGroup;
	private Button clear;
	ActionBar navbar;

	public SvolgimentoView(UtenteService u,CorsoService c,QuizService q){
		this.utenteS=u;
		this.quizS=q;
		this.corsoS=c;
		this.precedente=new Button("precedente");
		this.successivo=new Button("successivo");
		this.consegna=new Button("Consegna");
		H3 h=new H3("Domanda N° ");
		this.navbar=new ActionBar(precedente,h);
		navbar.AddButtonAtActionBar(successivo);
		navbar.AddButtonAtActionBar(consegna);
		precedente.setIcon(new Icon(VaadinIcon.ARROW_CIRCLE_LEFT_O));
		successivo.setIcon(new Icon(VaadinIcon.ARROW_CIRCLE_RIGHT_O));
		consegna.setIcon(new Icon(VaadinIcon.ARROW_CIRCLE_UP_O));
		add(navbar);

	}



	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		this.para=parameter;
		vedi();
		if(!(this.quiz==null)){
			domande=new ArrayList<Domanda>();
			domande.addAll(this.quiz.getDomande());
			rispostedate=new Risposta[domande.size()];
			if(!(domande.isEmpty())){
				timer = new SimpleTimer(new BigDecimal(this.quiz.getTempo()));
				timer.setHours(true);
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
				
				add(timer);
				updateDomanda(index);
				settaPulsanti();

			}
		}
	}





	private void settaPulsanti() {
		precedente.addClickListener(e->{
			if(index==0){
				Notification.show("E' la prima domanda non puoi andare indietro");
			}
			else
			{
				index=index-1;
				updateDomanda(index);
			}
		});
		successivo.addClickListener(e->{
			if(index==(domande.size()-1)){
				Notification.show("E' l'ultima Domanda");
			}
			else{
				index=index+1;
				updateDomanda(index);
			}
		});
		consegna.addClickListener(e->{
			Dialog dialog= new Dialog();
			Button consegna=new Button("Consegna");
			Button annulla=new Button("Torna indietro");
			HorizontalLayout hor=new HorizontalLayout();
			hor.add(consegna,annulla);
			dialog.add(hor);
			add(dialog);
			dialog.open();
			annulla.addClickListener(m-> dialog.close());
			consegna.addClickListener(m->{
				dialog.close();
				navbar.updateNdomanda(0);
				consegnaTest();
			});
		});
	}



	private void consegnaTest() {
		if((!(testo==null))&&(!(radioGroup==null))&&(!(clear==null))){
			timer.pause();
			remove(testo,radioGroup,clear);
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
		int numerototale=this.rispostedate.length;
		String numerototaleString=String.valueOf(numerototale);
		int numerogiusto=0;
		int numerosbagliate=0;
		int numeronondate=0;
		for(int i=0;i<numerototale;i++){
			if(!(rispostedate[i]==null)){
				if(rispostedate[i].getGiusta()==true){
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
		add(ver);
		
		
		
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
		setAlignItems(Alignment.CENTER);
		creazioneRisposte(i);
	}



	private void creazioneRisposte(int i) {
		Domanda domanda=domande.get(i);
		List<Risposta> risposte = new ArrayList<>();
		risposte.addAll(domanda.getRisposte());
		if(!(risposte.isEmpty())){
			if(!(radioGroup==null)&&(!(clear==null))){
				remove(radioGroup,clear);
			}
			radioGroup = new RadioButtonGroup<>();
			radioGroup.setItems(risposte);
			radioGroup.setRenderer(new TextRenderer<>(Risposta::getRisposta));
			if(!(rispostedate[i]==null)){
				radioGroup.setValue(rispostedate[i]);
			}
			radioGroup.addValueChangeListener(event -> {		
				if (event.getValue() == null) {
				}
				else {
					rispostedate[i]=event.getValue();
				}
			});
			 clear = new Button("Clear", event -> {
				    radioGroup.setValue(null);
				    rispostedate[i]=null;
				});
			add(radioGroup,clear);
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
