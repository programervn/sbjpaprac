package com.thaipd.sbjpaprac.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "book_pages")
public class BookPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int pageNumber;
    private String content;
    private String chapter;

    //default Many To One is EAGER
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    @JsonBackReference
    private Book book;

    public BookPage() {
    }

    public BookPage(int pageNumber, String content, String chapter, Book book) {
        this.pageNumber = pageNumber;
        this.content = content;
        this.chapter = chapter;
        this.book = book;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookPage page = (BookPage) o;
        return id.equals(page.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Page{" +
                "id=" + this.id +
                ", number=" + this.pageNumber +
                ", content='" + this.content + '\'' +
                ", chapter='" + this.chapter + '\'' +
                ", book_id=" + this.book.getId() +
                //", book=" + book +
                '}';
    }
}
