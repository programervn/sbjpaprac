package com.thaipd.sbjpaprac.service;

import com.thaipd.sbjpaprac.entity.Book;
import com.thaipd.sbjpaprac.entity.BookPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    public Book findBookInfo(Long bookID);
    public Set<BookPage> getPage(Book book);
    public List<BookPage> findPageByBook(Book book);
}
