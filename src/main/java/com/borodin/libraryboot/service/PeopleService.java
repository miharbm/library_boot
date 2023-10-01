package com.borodin.libraryboot.service;


import com.borodin.libraryboot.models.Book;
import com.borodin.libraryboot.models.Person;
import com.borodin.libraryboot.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional()
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Transactional(readOnly = true)
    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById( id );
        return foundPerson.orElse( null );
    }

    @Transactional(readOnly = true)
    public Optional<Person> findOne(String email) {
        return peopleRepository.findByEmail( email );
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save( person );
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId( id );
        peopleRepository.save( updatedPerson );
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById( id );
    }

    @Transactional(readOnly = true)
    public List<Book> getBooks(int id){
        Optional<Person> person = peopleRepository.findById( id );

        if(person.isPresent()){
            Hibernate.initialize(person.get().getBooks());
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public List<Book> getBooks(Person person){
        Hibernate.initialize( person.getBooks() );
        return person.getBooks();
    }

}
