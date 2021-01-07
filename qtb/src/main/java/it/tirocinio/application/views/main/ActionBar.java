package it.tirocinio.application.views.main;

import com.flowingcode.vaadin.addons.simpletimer.SimpleTimer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Quiz;

import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
@CssImport("./styles/views/hello/hello-view.css")
public class ActionBar extends HorizontalLayout {
	private HorizontalLayout h=new HorizontalLayout();
	private H3 numerocambio;

	public ActionBar(Button button,H3 h3){
		setId("prof-navbar");
		setPadding(true);
		this.h.add(h3);
		h3.getStyle().set("color", "#ffffff");
		setVerticalComponentAlignment(Alignment.CENTER,h);
		add(this.h);
		button.setIcon(new Icon(VaadinIcon.PLUS));
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		button.getElement().getStyle().set("margin-left", "auto");
		setVerticalComponentAlignment(Alignment.CENTER,button);
		add(button);
	}
	public ActionBar(Button button, TextField filter) {
		setId("prof-navbar");
		setPadding(true);	
		setVerticalComponentAlignment(Alignment.CENTER,filter);
		filter.getStyle().set("background-color", "#ffffff");
		filter.setPlaceholder("cerca corso");
		add(filter);
		button.setIcon(new Icon(VaadinIcon.PLUS));
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		button.getElement().getStyle().set("margin-left", "auto");
		setVerticalComponentAlignment(Alignment.CENTER,button);
		add(button);
	}
	public ActionBar(Button button, ComboBox<Corso> corsi) {
		setId("prof-navbar");
		setPadding(true);	
		H3 h3 =new H3("seleziona corso : ");
		h3.getStyle().set("color", "#ffffff");
		setVerticalComponentAlignment(Alignment.CENTER,corsi,h3);
		corsi.getStyle().set("background-color", "#ffffff");	
		add(h3,corsi);
		button.setIcon(new Icon(VaadinIcon.PLUS));
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		button.getElement().getStyle().set("margin-left", "auto");
		setVerticalComponentAlignment(Alignment.CENTER,button);
		add(button);
	}
	public ActionBar(ComboBox<Corso> corsi,Button button) {
		setId("prof-navbar");
		setPadding(true);	
		H3 h3 =new H3("Scegli azienda : ");
		h3.getStyle().set("color", "#ffffff");
		setVerticalComponentAlignment(Alignment.CENTER,corsi,h3);
		corsi.getStyle().set("background-color", "#ffffff");	
		add(h3,corsi);
		button.setIcon(new Icon(VaadinIcon.PLUS));
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		button.getElement().getStyle().set("margin-left", "auto");
		setVerticalComponentAlignment(Alignment.CENTER,button);
		add(button);
		
	}
	

	
	
	public ActionBar(Button button, ComboBox<Quiz> quizs,int i) {
		setId("prof-navbar");
		setPadding(true);	
		H3 h3 =new H3("Test : ");
		h3.getStyle().set("color", "#ffffff");
		setVerticalComponentAlignment(Alignment.CENTER,quizs,h3);
		quizs.getStyle().set("background-color", "#ffffff");	
		add(h3,quizs);
		button.setIcon(new Icon(VaadinIcon.PLUS));
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		button.getElement().getStyle().set("margin-left", "auto");
		setVerticalComponentAlignment(Alignment.CENTER,button);
		add(button);
	}
	public ActionBar(Button button, TextField quizs,int i) {
		setId("prof-navbar");
		setPadding(true);	
		H3 h3 =new H3("Test : ");
		h3.getStyle().set("color", "#ffffff");
		setVerticalComponentAlignment(Alignment.CENTER,quizs,h3);
		quizs.getStyle().set("background-color", "#ffffff");	
		add(h3,quizs);
		button.setIcon(new Icon(VaadinIcon.PLUS));
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		button.getElement().getStyle().set("margin-left", "auto");
		setVerticalComponentAlignment(Alignment.CENTER,button);
		add(button);
	}
	public ActionBar(Button button, H3 h, SimpleTimer timer) {
		setId("prof-navbar");
		setPadding(true);
		this.h.add(h);
		h.getStyle().set("color", "#ffffff");
		timer.setHours(true);	
		timer.setFractions(false);
		timer.getStyle().set("color", "#ffffff");
		timer.getStyle().set("font-size", "var(--lumo-font-size-xl)");
		setVerticalComponentAlignment(Alignment.CENTER,h,timer);
		timer.getElement().getStyle().set("margin-left", "auto");
		add(this.h);
		add(timer);
		button.setIcon(new Icon(VaadinIcon.PLUS));
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		setVerticalComponentAlignment(Alignment.CENTER,button);
		add(button);
	}
	public void AddButtonAtActionBar(Button button){
		button.setIcon(new Icon(VaadinIcon.PLUS));
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);		
		setVerticalComponentAlignment(Alignment.CENTER,button);
		add(button);
	}
	public void updateNdomanda(int numero) {
		String a = String.valueOf(numero);
		if(!(numerocambio==null)){
			this.h.remove(numerocambio);
		}
		if(numero!=0){
			numerocambio = new H3(a);
			this.numerocambio.getStyle().set("color", "#ffffff");
			setVerticalComponentAlignment(Alignment.CENTER,this.numerocambio);
			this.h.add(this.numerocambio);
		}

	}
}
