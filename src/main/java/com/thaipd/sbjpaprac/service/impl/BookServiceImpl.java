package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.entity.Book;
import com.thaipd.sbjpaprac.entity.BookPage;
import com.thaipd.sbjpaprac.repository.BookRepository;
import com.thaipd.sbjpaprac.repository.PageRepository;
import com.thaipd.sbjpaprac.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    @Autowired
    BookRepository bookRepository;
    @Autowired
    PageRepository pageRepository;

    public Book findBookInfo(Long bookID) {
        // create a new book
        Optional<Book> optionalBook = bookRepository.findById(bookID);
        if (!optionalBook.isPresent()) {
            logger.error("Cannot find book with id {}", bookID);
            return null;
        }
        Book book = optionalBook.get();
        logger.info("Book: {}", book);
        return book;
    }

    public Set<BookPage> getPage(Book book) {
        Set<BookPage> bookPages = book.getPages();
        return bookPages;
    }

    public List<BookPage> findPageByBook(Book book) {
        return pageRepository.findByBook(book);
    }
}
