package com.thaipd.sbjpaprac.service;

import com.thaipd.sbjpaprac.entity.Book;
import com.thaipd.sbjpaprac.entity.BookPage;


import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    public Optional<Book> findBookByID(Long id);
    public Book findBookInfo(Long bookID);
    public Set<BookPage> getPage(Book book);
    public List<BookPage> findPageByBook(Book book);
}
