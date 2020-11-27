package it.tirocinio.application.views.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
@CssImport("./styles/views/hello/hello-view.css")
public class ActionBar extends HorizontalLayout {

	public ActionBar(Button button){
		setId("prof-navbar");
		setPadding(true);
		H3 h=new H3("");
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
	public void AddButtonAtActionBar(Button button){
    	button.setIcon(new Icon(VaadinIcon.PLUS));
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);		
		setVerticalComponentAlignment(Alignment.CENTER,button);
		add(button);
	}
}
