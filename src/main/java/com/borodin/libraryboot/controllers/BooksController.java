package com.borodin.libraryboot.controllers;



import com.borodin.libraryboot.models.Book;
import com.borodin.libraryboot.models.Person;
import com.borodin.libraryboot.service.BooksService;
import com.borodin.libraryboot.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public BooksController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Optional<Integer> page,
                        @RequestParam(value = "books_per_page", required = false) Optional<Integer> books_per_page,
                        @RequestParam(value = "sort_by_year", defaultValue = "false") Boolean sort_by_year
                        ) {
        if(page.isPresent() && books_per_page.isPresent()){
            if(page.get() < 1 || books_per_page.get() < 1){ // не придумал ничего лучше
                model.addAttribute( "books", booksService.findAll() );
                return "books/index";
            }

            if(sort_by_year){
                model.addAttribute( "books",
                        booksService.findPaginationAndSort( page.get() - 1, books_per_page.get() ) );
            } else {
                model.addAttribute( "books",
                        booksService.findPagination( page.get() - 1, books_per_page.get() ) );
            }
        } else {
            model.addAttribute( "books", booksService.findAll() );
        }
        return "books/index";
    }

//    @GetMapping()
//    public String index(Model model) {
//        model.addAttribute( "books", booksService.findAll() );
//        return "books/index";
//    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){

        Book book = booksService.findOne( id );
        model.addAttribute( "book", book );
        if(book.getOwner() == null){
            model.addAttribute( "people", peopleService.findAll() );
        } else {
            model.addAttribute( "owner", peopleService.findOne(  book.getOwner().getId() ) );
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute( "book", new Book() );
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") Book book,
                         BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "books/new";
        }

        booksService.save( book );
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable int id) {
        model.addAttribute( "book", booksService.findOne( id ) );
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") int id){
        if(bindingResult.hasErrors()){
            return "books/edit";
        }
        booksService.update( id, book );
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        booksService.delete( id );
        return "redirect:/books";
    }

    @PatchMapping("/{id}/lend")
    public String lend(@ModelAttribute("person")Person person, @PathVariable("id") int id_book){
        booksService.setOwner( id_book, person );
        return "redirect:/books/" + id_book;
    }

    @PatchMapping("/{id}/return")
    public String returnBook(@PathVariable("id") int id){
        booksService.setOwner( id, null );
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam(value = "s", required = false) String search){

        model.addAttribute( "books", booksService.findByNameContains( search ) );
        return "/books/search";
    }
}
