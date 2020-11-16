package it.tirocinio.application.views.hello;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.tirocinio.application.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "hello", layout = MainView.class)
@PageTitle("hello")
@CssImport("./styles/views/hello/hello-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class HelloView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    public HelloView() {
        setId("hello-view");
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        add(name, sayHello);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);
        sayHello.addClickListener( e-> {
            Notification.show("Hello " + name.getValue());
        });
    }

}
