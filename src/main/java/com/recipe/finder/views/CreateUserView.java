package com.recipe.finder.views;

import com.recipe.finder.external.UserDto;
import com.recipe.finder.external.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("createUser")
public class CreateUserView extends FormLayout {
    private final UserService userService;

    private transient UserDto userDto=new UserDto();

    @PropertyId("name")
    private final TextField username = new TextField("Username");

    @PropertyId("email")
    private final TextField email = new TextField("Mail");

    @PropertyId("password")
    private final TextField password = new TextField("Password");

    private final Button save = new Button("Save");

    private final Binder<UserDto> binder = new Binder<>(UserDto.class);
    private final Logger logger = LoggerFactory.getLogger(CreateUserView.class);


    public CreateUserView(UserService userService){
        this.userService = userService;

        HorizontalLayout fields = new HorizontalLayout(username,email,password);
        HorizontalLayout button = new HorizontalLayout(save);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event ->save());


        binder.bindInstanceFields(this);
        add(fields,button);

    }

    private void save(){
        try{
            binder.writeBean(userDto);
            userService.createUser(userDto.toDomain());
            logger.info("createUserView {}",userDto);
            save.getUI().flatMap(ui -> ui.navigate(LoginView.class));
        }catch (ValidationException e){
            throw new RuntimeException(e);
        }
    }
}
