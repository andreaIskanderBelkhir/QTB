package it.tirocinio.application.views.hello;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.testbench.RadioButtonElement;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.view.form.DomandaForm;
import it.tirocinio.application.view.form.RispostaForm;
import it.tirocinio.application.view.form.ScegliTestForm;
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

@Route(value = "domande2elenco", layout = MainView.class)
@PageTitle("Gestione delle domande 2")
@CssImport("./styles/views/hello/hello-view.css")
public class Domande2view extends VerticalLayout {
	private Set<Risposta> risposte;
	private String nome ;
	private Utente docente;
	private CorsoService corsoS;
	private UtenteService utenteS;
	private QuizService quizS;
	private DomandaService domandaS;
	private RispostaService rispostaS;
	private Domanda domanda;
	private Quiz test;
	Boolean salvato=true;
	ScegliTestForm testForm;
	HorizontalLayout hor=new HorizontalLayout();
	Grid<Domanda> griddomanda= new Grid<>(Domanda.class);
	Grid<Risposta> gridrisposta= new Grid<>(Risposta.class);
	ComboBox<Quiz> quizs ;
	TextField nomeQuizSopra;
	private DomandaForm domandaForm;
	private Div div2;



	public Domande2view(CorsoService s, UtenteService u,QuizService q,DomandaService d,RispostaService r){

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
		quizs.addClassName("my-combo");
		quizs.setReadOnly(true);
		quizs.addValueChangeListener(e->updatetextfield());
		nomeQuizSopra=new TextField();
		nomeQuizSopra.setReadOnly(true);

		this.domandaForm=new DomandaForm(domandaS, corsoS, quizS, docente);
		testForm=new ScegliTestForm(quizS, utenteS,domandaS);
		Button creazioneDbutton = new Button("Nuovo",e->nuovaDomanda());
		Button modificaDbutton = new Button("Salva",e->salvaDomanda());
		//Button eliminaDbutton = new Button("Elimina",e->domandaForm.Elimina(quizs.getValue(),this.domanda,griddomanda));
		Button selezionaTbutton = new Button("Seleziona test",e->selezioneTest());
		ActionBar navbar2=new ActionBar(selezionaTbutton,nomeQuizSopra,1);
		navbar2.AddButtonAtActionBar(creazioneDbutton);
		navbar2.AddButtonAtActionBar(modificaDbutton);

		add(navbar2);

		hor.setHeight("100%");
		hor.setWidthFull();
		ConfigureGridD();
		griddomanda.setSizeFull();
		add(domandaForm,testForm);
		Div div1=new Div();
		div1.setSizeFull();
		div1.add(griddomanda);
		this.div2 =new Div();
		div2.setSizeFull();

		hor.add(div1,div2);
		add(hor);
	}






	private void nuovaDomanda() {
		this.div2.removeAll();
		domandaForm.Nuovo(quizs.getValue(),griddomanda);
	}






	private void selezioneTest() {
		this.div2.removeAll();
		testForm.selezionaTest(docente,quizs,griddomanda);
	}






	private void updatetextfield() {
		if(!(testForm.test==null)){
			this.test=testForm.test;
			String a=new String(this.test.getNomeQuiz()+"("+ this.test.getCorsoAppartenenza().getNomeCorso()+ ")");
			nomeQuizSopra.setValue(a);
		}
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


		griddomanda.setColumns("id");
		griddomanda.getColumnByKey("id").setFlexGrow(0);
		griddomanda.addColumn(domanda->{
			return domanda.getNomedomanda();
		}).setHeader("Nome").setFlexGrow(0);
		griddomanda.addColumn(domanda->{
			return domanda.getDescrizionedomanda();
		}).setHeader("Testo").setFlexGrow(7);
		griddomanda.addComponentColumn(item-> eliminatato(griddomanda,item)).setHeader("Elimina");
		griddomanda.setWidth("98%");
		griddomanda.asSingleSelect().addValueChangeListener(event->{	
			this.domanda=event.getValue();	
			if(event.getValue()!=null){
				if(this.salvato==true){
					this.domanda=event.getValue();
					updateviewDomanda(event.getValue());}
				else
				{
					Dialog dia=new Dialog();
					Button continua=new Button("continua senza salvare");
					Button annulla=new Button("annulla");
					continua.addClickListener(e->{
						this.domanda=event.getValue();
						this.salvato=true;
						updateviewDomanda(event.getValue());
						dia.close();
					});
					annulla.addClickListener(e->{
						if(event.getOldValue()!=null){
						this.domanda=event.getOldValue();
						}
						dia.close();
					});
					VerticalLayout veer=new VerticalLayout();
					HorizontalLayout horrr=new HorizontalLayout();
					horrr.add(continua,annulla);
					veer.add("ATTENZIONE non hai salvato");
					veer.add(horrr);
					dia.add(veer);
					dia.open();
					add(dia);
				}
			}
		});

	}

	private void updateviewDomanda(Domanda domanda) {

		if(domanda==null){

		}
		else
		{

			div2.removeAll();
			this.div2.getStyle().set("padding","true");
			this.div2.getStyle().set("margin-left", "10px");
			HorizontalLayout nomeDom=new HorizontalLayout();
			this.div2.add(nomeDom);
			H3 nomeh=new H3("Nome : ");	
			TextField nomeDomandamod=new TextField();
			nomeDomandamod.setValue(domanda.getNomedomanda());
			nomeDomandamod.addValueChangeListener(e->{
				this.salvato=false;
				domanda.setNomedomanda(nomeDomandamod.getValue());
			});
			nomeDom.setVerticalComponentAlignment(Alignment.CENTER,nomeh,nomeDomandamod);   
			nomeDom.add(nomeh,nomeDomandamod);
			TextArea testoDomanda=new TextArea();
			testoDomanda.setValue(domanda.getDescrizionedomanda());
			testoDomanda.addValueChangeListener(e->{
				this.salvato=false;	
				domanda.setDescrizionedomanda(testoDomanda.getValue());
			});
			testoDomanda.setWidthFull();
			this.div2.add(testoDomanda);
			HorizontalLayout h=new HorizontalLayout();
			H3 h31=new H3("Corretta");			
			H3 h32=new H3("Testo Risposta");	
			h32.getStyle().set("margin-left", "80px");
			h.add(h31,h32);
			this.div2.add(h);
			VerticalLayout verti = new VerticalLayout();
			this.div2.add(verti);
			risposte=this.domandaS.findRisposte(domanda);
			for(Risposta r:risposte){
				HorizontalLayout hor=new HorizontalLayout();
				hor.setWidthFull();
				Checkbox giusto = new Checkbox();
				giusto.setValue(r.getGiusta());
				giusto.addClickListener(e->{
					this.salvato=false;	
					r.setGiusta(giusto.getValue());
				});
				TextField testoR= new TextField();
				testoR.setValue(r.getRisposta());
				testoR.getStyle().set("margin-left", "50px");
				testoR.setWidthFull();
				testoR.addValueChangeListener(e->{
					this.salvato=false;	
					r.setRisposta(testoR.getValue());
				});
				hor.add(giusto,testoR);
				verti.add(hor);

			}

			Button aggiungiRisp = new Button("aggiungi risposta");
			aggiungiRisp.addClickListener(e->{
				creaRisposta(verti);
			});
			aggiungiRisp.getStyle().set("margin-left", "200px");
			this.div2.add(aggiungiRisp);
		}
	}



	private void creaRisposta(VerticalLayout verti) {
		this.salvato=false;	
		Notification.show("aggiungi una risposta e poi salva (se crei piu di una risposta nuova per volta solo la prima verra salvata) ");
		HorizontalLayout hor=new HorizontalLayout();
		Risposta ri=new Risposta();
		ri.setRisposta("");
		ri.setDomandaApparteneza(domanda);
		ri.setGiusta(false);	
		hor.setWidthFull();
		Checkbox giusto = new Checkbox();
		giusto.setValue(false);
		giusto.addClickListener(e->ri.setGiusta(giusto.getValue()));
		TextField testoR= new TextField();
		testoR.setValue("");
		testoR.getStyle().set("margin-left", "50px");
		testoR.setWidthFull();
		testoR.addValueChangeListener(e->ri.setRisposta(testoR.getValue()));
		risposte.add(ri);		
		hor.add(giusto,testoR);
		verti.add(hor);
	}
	private void salvaDomanda() {

		for(Risposta r:risposte){
			domanda.getRisposte().add(r);
			this.rispostaS.save(r);
		}		
		Long id=domanda.getId();
		this.domandaS.save(domanda);
		griddomanda.setItems(this.domandaS.findByQuiz(quizs.getValue()));
		this.salvato=true;	
		griddomanda.select(this.domandaS.findById(id));

	}


	private Button eliminatato(Grid<Domanda> griddomanda2, Domanda item) {
		Button eliminaDbutton = new Button("X",e->domandaForm.Elimina(quizs.getValue(),item,griddomanda));
		eliminaDbutton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		eliminaDbutton.addThemeVariants(ButtonVariant.LUMO_SMALL);
		return eliminaDbutton;
	}



}
