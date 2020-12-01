package it.tirocinio.application.views.main;

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

	public ActionBar(Button button,H3 h){
		setId("prof-navbar");
		setPadding(true);
		h.getStyle().set("color", "#ffffff");
		setVerticalComponentAlignment(Alignment.CENTER,h);
        add(h);
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
	public ActionBar(Button button, ComboBox<Quiz> quizs,int i) {
		setId("prof-navbar");
		setPadding(true);	
		H3 h3 =new H3("seleziona Test : ");
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
	public void AddButtonAtActionBar(Button button){
    	button.setIcon(new Icon(VaadinIcon.PLUS));
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);		
		setVerticalComponentAlignment(Alignment.CENTER,button);
		add(button);
	}
}
