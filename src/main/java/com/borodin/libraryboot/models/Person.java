package com.borodin.libraryboot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Person")
public class Person {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name should not  be empty")
    @Size(min = 2, max = 30, message = "Name's length should be between 2 and 30")
//    @Pattern( regexp = "[A-Z]\\w+ [A-Z]\\w+ [A-Z]\\w+", message = "Your name should be in this format: " +
      //      "Бородин Михаил Сергеевич")
    @Column(name = "name")
    private String name;

//    @NotEmpty(message = "Date of birth should not be empty")
    @Column(name = "date_of_birth")
    @Temporal( TemporalType.DATE )
    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date dateOfBirth;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "email should be valid")
    @Column(name = "email")
    private String email;


    @Pattern( regexp = "[A-Z]\\w+, [A-Z]\\w+, \\d{6}", message = "Your address should be in this format: " +
            "Country, City, Postal Code")
    @Column(name = "address")
    private String address;



    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person(){}

    public Person(int id, String name, Date dateOfBirth, String email){
        this.id = id;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }


    public List<Book> getBooks() {

        return this.books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
