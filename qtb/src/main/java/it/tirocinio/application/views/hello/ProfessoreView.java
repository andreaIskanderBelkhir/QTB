package it.tirocinio.application.views.hello;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.tirocinio.application.views.main.MainView;

@Route(value = "professore", layout = MainView.class)
@PageTitle("ProfPage")
@CssImport("./styles/views/hello/hello-view.css")
public class ProfessoreView extends VerticalLayout{

}
