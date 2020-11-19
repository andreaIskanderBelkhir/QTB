package it.tirocinio.application.view;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import it.tirocinio.application.views.main.MainView;

@Route(value="Homepage")
@PageTitle("Homepage")
@RouteAlias(value = "", layout = MainView.class)
public class HomePageView extends HorizontalLayout{
   
	public HomePageView(){
		setAlignItems(FlexComponent.Alignment.START);
		setPadding(true);
		add("sei sulla home");
	}
}
