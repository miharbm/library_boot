package com.borodin.libraryboot.controllers;


import com.borodin.libraryboot.models.Book;
import com.borodin.libraryboot.models.Person;
import com.borodin.libraryboot.service.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute( "people", peopleService.findAll() );
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Person person = peopleService.findOne( id );
        List<Book> books = peopleService.getBooks( id );
        System.out.println(books.isEmpty());
        for(Book book : books){
            System.out.println(book);
        }
        model.addAttribute( "person", person );
        model.addAttribute( "books", books );
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(Model model) {
        model.addAttribute( "person", new Person() );
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return "people/new";
        }

        peopleService.save( person );
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute( "person", peopleService.findOne( id ) );
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if(bindingResult.hasErrors()){
            return "people/edit";
        }
        peopleService.update( id, person );
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        peopleService.delete( id );
        return "redirect:/people";
    }
}