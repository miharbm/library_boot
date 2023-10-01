package com.borodin.libraryboot.util;


import com.borodin.libraryboot.models.Person;
import com.borodin.libraryboot.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {

        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals( clazz );
    }

    @Override
    public void validate(Object target, Errors errors) {

        Person person = (Person) target;

        if(peopleService.findOne( person.getEmail()).isPresent()){
            errors.rejectValue( "email", "", "This email is already in use" );
        }

        if (!Character.isUpperCase(person.getName().codePointAt(0))){
            errors.rejectValue("name", "", "Name should start with a capital letter");
        }
    }
}
