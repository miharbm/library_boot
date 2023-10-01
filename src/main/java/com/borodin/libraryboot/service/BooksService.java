package com.borodin.libraryboot.service;


import com.borodin.libraryboot.models.Book;
import com.borodin.libraryboot.models.Person;
import com.borodin.libraryboot.repositories.BooksRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional("transactionManager")
public class BooksService {

    private final BooksRepository booksRepository;
    private final PeopleService peopleService;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleService peopleService) {
        this.booksRepository = booksRepository;
        this.peopleService = peopleService;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll(){
        return booksRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book findOne(int id){
        Optional<Book> foundBook = booksRepository.findById( id );
        return foundBook.orElse( null );
    }

    public void save(Book book){
        booksRepository.save( book );
    }

    public void update(int id, Book updatedBook){
        updatedBook.setId( id );
        booksRepository.save( updatedBook );
    }

    public void delete(int id){
        booksRepository.deleteById( id );
    }

    public void setOwner(int id, Person person){

        if(person == null){
            setOwnerNull( id );
            return;
        }

        Book book = this.findOne( id );

        book.setOwner( person );
        book.setDateOwned( new Date() );

        Hibernate.initialize( person.getBooks() );

        if(person.getBooks() == null){
            person.setBooks( new ArrayList<>() );
        }
        person.getBooks().add( book );

        this.update( id, book );

    }

    public void setOwnerNull(int id){
        Book book = this.findOne( id );

        Hibernate.initialize(book.getOwner().getBooks());

        peopleService.getBooks( book.getOwner() ).remove( book );
        book.setOwner(null);

        this.update( id, book );

    }

    @Transactional(readOnly = true)
    public List<Book> findPagination(int page, int itemsPerPage){
        return booksRepository.findAll( PageRequest.of( page, itemsPerPage ) ).getContent();
    }
    @Transactional(readOnly = true)
    public List<Book> findPaginationAndSort(int page, int itemsPerPage){
        return booksRepository.findAll( PageRequest.of( page, itemsPerPage, Sort.by( "year" ) ) ).getContent();
    }

    public List<Book> findByNameContains(String name){
        return booksRepository.findByNameContains( name );
    }



}
