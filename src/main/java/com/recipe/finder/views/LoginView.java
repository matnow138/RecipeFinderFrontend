package com.recipe.finder.views;

import com.recipe.finder.external.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Login Page")
public class LoginView extends VerticalLayout {

    @PropertyId("name")
    private final TextField name = new TextField("Login");

    @PropertyId("password")
    private final TextField password = new TextField("Password");

    private final Button login = new Button("Login");
    private final Button createUser = new Button("Create account");

    private final UserService userService;


    public LoginView(UserService userService){
        this.userService=userService;
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout fields = new VerticalLayout(name,password);
        VerticalLayout buttons = new VerticalLayout(login,createUser);

        createUser.addClickListener(event -> createUser.getUI()
                .flatMap(ui -> ui.navigate(CreateUserView.class)));
        login.addClickListener(event ->{
            userService.authorizeUser(name.getValue(),password.getValue());
            if(userService.checkIfCookiePresent("Finder-token")){
                login.getUI()

                        .flatMap(ui -> ui.navigate(Frontend.class));
            }
        });

        add(new H1("Login Page"),name,password,login,createUser);

    }

}
