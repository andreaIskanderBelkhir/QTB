package it.tirocinio.application.views.hello;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.views.main.MainView;

@Route(value = "studente", layout = MainView.class)
@PageTitle("studentePage")
@CssImport("./styles/views/hello/hello-view.css")
public class StudenteView extends VerticalLayout {

}
