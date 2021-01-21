package it.tirocinio.application.views.hello;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.tirocinio.application.views.main.MainView;
import it.tirocinio.backend.service.UtentePendingService;
import it.tirocinio.entity.UtentePending;

import com.vaadin.flow.router.RouteAlias;

@Route(value = "admin", layout = MainView.class)
@PageTitle("admin")
@CssImport("./styles/views/hello/hello-view.css")
public class AdminView extends HorizontalLayout {
	

    private UtentePendingService utentePendingService;

    Grid<UtentePending> grid = new Grid<>(UtentePending.class);

    


    public AdminView(UtentePendingService utenteService) {
    	this.utentePendingService=utenteService;
    	HorizontalLayout hor = new HorizontalLayout();
        setId("hello-view");
        configureGrid(); 
        add(hor);  
        add(grid);
        updateList();
    }
    

	private void updateList() {
		grid.setItems(utentePendingService.findDaValidare(false));
		
	}




	private void configureGrid() {
		grid.removeColumnByKey("ID");
		grid.removeColumnByKey("attivato");
		grid.setColumns("nome","email","descizione");
		grid.addComponentColumn(item-> createValited(grid,item)).setHeader("convalida");
		grid.setWidth("98%");


		
	}


	@SuppressWarnings("unchecked")
	private Checkbox createValited(Grid<UtentePending> grid2, UtentePending item) {
		// TODO Auto-generated method stub
		
		Checkbox check = new Checkbox();
		check.addClickListener(click->{
			utentePendingService.setValide(item);
		});
		return check;
	}

}
