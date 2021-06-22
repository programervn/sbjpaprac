package com.thaipd.sbjpaprac.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/*
https://hellokoding.com/jpa-one-to-many-relationship-mapping-example-with-spring-boot-maven-and-mysql/
When to use @OneToMany
In summary, you can use @OneToMany if the child collection size is limited, otherwise, if the child collection can grow to a lot of items, consider to

Don't retrieve @OneToMany child collection directly from the parent, you can retrieve via custom queries on the repositories like the step 2 above

Don't use CascadeType.REMOVE or CascadeType.ALL with @OneToMany
*/
@Entity
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    //@OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "library", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Set<LibraryBook> books = new HashSet<>();

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

    //rem because of not using cascade.ALL
//    public Set<LibraryBook> getBooks() {
//        return books;
//    }

    public void setBooks(Set<LibraryBook> books) {
        this.books = books;

        for(LibraryBook b : books) {
            b.setLibrary(this);
        }
    }
}
