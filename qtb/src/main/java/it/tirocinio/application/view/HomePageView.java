package it.tirocinio.application.view;



import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;


import it.tirocinio.application.views.main.MainView;

@Route(value="Homepage",layout=MainView.class)
@PageTitle("Homepage")
@RouteAlias(value = "", layout = MainView.class)
public class HomePageView extends HorizontalLayout{
	String nome ;
   
	public HomePageView(){
		setAlignItems(FlexComponent.Alignment.START);
		setPadding(true);
		add("sei sulla home   ");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ( principal instanceof UserDetails){
			this.nome = ((UserDetails)principal).getUsername();
		}
		else{
			this.nome = principal.toString();
		}
		add(nome);

	}
}
