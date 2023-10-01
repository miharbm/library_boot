package com.borodin.libraryboot.models;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@Table(name = "Book")
public class  Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name should not  be empty")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Author's name should not be empty")
    @Size(min = 2, max = 30, message = "Name's length should be between 2 and 30")
//    @Pattern( regexp = "[A-Z]\\w+ [A-Z]\\w+ [A-Z]\\w+", message = "Your name should be in this format: " +
//            "Бородин Михаил Сергеевич")
    @Column(name = "author")
    private String author;

    @Min( value = 1700)
    @Max( value = 2023)
    @Column(name = "year")
    private int year;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "date_owned")
    @Temporal( TemporalType.TIMESTAMP )
    private Date dateOwned;

    @Transient
    private Boolean overdue = null;


    public Book(){}

    @PostConstruct
    private void doInit(){

        if (dateOwned != null){
            Calendar calendar = new GregorianCalendar();
            calendar.add( Calendar.DAY_OF_MONTH, -10 );
            this.overdue = dateOwned.before( calendar.getTime() );
        }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getDateOwned() {
        return dateOwned;
    }

    public void setDateOwned(Date dateOwned) {
        this.dateOwned = dateOwned;
    }

    public Boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(Boolean overdue) {
        this.overdue = overdue;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", owner=" + owner +
                ", dateOwned=" + dateOwned +
                ", overdue=" + overdue +
                '}';
    }
}
