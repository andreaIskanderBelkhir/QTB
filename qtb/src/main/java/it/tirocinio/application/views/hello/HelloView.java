package it.tirocinio.application.views.hello;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.UtenteService;
import it.tirocinio.entity.Utente;

import com.vaadin.flow.router.RouteAlias;

@Route(value = "hello", layout = MainView.class)
@PageTitle("hello")
@CssImport("./styles/views/hello/hello-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class HelloView extends HorizontalLayout implements AfterNavigationObserver {
	
    @Autowired
    private UtenteService utenteService;
    
    private TextField name;
    private Button sayHello;
    private Grid<Utente> utenti = new Grid<>(Utente.class);
    private Binder<Utente> binder;

    


    public HelloView(UtenteService utenteService1) {
    	this.utenteService=utenteService1;
    	HorizontalLayout hor = new HorizontalLayout();
        setId("hello-view");
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        setVerticalComponentAlignment(Alignment.END, name, sayHello);
        sayHello.addClickListener( e-> {
            Notification.show("Hello " + name.getValue());
        });
        hor.add(name, sayHello);
        add(hor);
        configureGrid();
        hor.add(utenti);
        updategrid();
    }



	private void updategrid() {
		// TODO Auto-generated method stub
		utenti.setItems(utenteService.findAll());
	}



	private void configureGrid() {
		utenti.setSizeFull();
		utenti.setColumns("id","nome","password","id_ruolo");
		
		binder = new Binder<>(Utente.class);
		binder.bindInstanceFields(this);
		binder.setBean(new Utente());
		
		
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		// TODO Auto-generated method stub
		utenti.setItems(utenteService.findAll());
		
	}   


}
