package com.thaipd.sbjpaprac;

import com.thaipd.sbjpaprac.entity.Book;
import com.thaipd.sbjpaprac.entity.BookPage;
import com.thaipd.sbjpaprac.repository.BookRepository;
import com.thaipd.sbjpaprac.repository.BookPageRepository;
import com.thaipd.sbjpaprac.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

/*
references:
    https://attacomsian.com/blog/spring-data-jpa-one-to-many-mapping

 */

@SpringBootApplication
@EnableJpaAuditing
public class SbjpapracApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(SbjpapracApplication.class);
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookPageRepository pageRepository;

    @Autowired
    BookService bookService;

    public static void main(String[] args) {
        logger.info("STARTING THE APPLICATION");
        SpringApplication.run(SbjpapracApplication.class, args);
        logger.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        logger.info("EXECUTING : command line runner");
        //saveBook();
        //savePage();
        //findBookPage();
        //findBookInfo();

    }

    public void findTestBookEager() {
        Book book = bookService.findBookByID(1L).get();
        logger.info("Book infor: {}", book);
        logger.info("Page(s): {}", book.getPages());
    }

    public void findTestPageEager() {
        BookPage bookPage = pageRepository.findById(1L).get();
        logger.info("************ Page information: {}", bookPage);
        Book book = bookPage.getBook();
        logger.info("************ Book information from page: {}", book);
    }

    public void findTestPageLazing() {
        BookPage bookPage = pageRepository.findById(1L).get();
        logger.info("************ Page information: {}", bookPage);
        //you get page, but not book
        //Book book = bookRepository.findById(bookPage.getBook().getId()).get();
        Book book = bookRepository.getById(bookPage.getBook().getId());
        logger.info("************ Book information from page: {}", book);
    }

    public void findBookPage() {
        Long bookID = 1L;
        // create a new book
        Optional<Book> optionalBook = bookRepository.findById(bookID);
        if (!optionalBook.isPresent()) {
            logger.error("Cannot find book with id {}", bookID);
            return;
        }
        Book book = optionalBook.get();
        List<BookPage> bookPageList = pageRepository.findByBook(book);
        logger.info("Page(s) of book : {}", book);
        for (BookPage page : bookPageList) {
            logger.info("{}", page);
            Book b = page.getBook();
            logger.info("Book: {}, {}, {}", b.getId(), b.getAuthor(), b.getIsbn());
        }
    }

    public void findBookInfo() {
        Long bookID = 1L;
        // create a new book
        Optional<Book> optionalBook = bookRepository.findById(bookID);
        if (!optionalBook.isPresent()) {
            logger.error("Cannot find book with id {}", bookID);
            return;
        }
        Book book = optionalBook.get();
        logger.info("Book: {}", book);
        List<BookPage> bookPageSet = bookService.findPageByBook(book);
        for (BookPage bookPage : bookPageSet) {
            logger.info("Page(s): {}", bookPage);
        }
    }

    public void saveBook() {
        // create a new book
        Book book = new Book("Java Spring Boot JPA", "John Doe", "123456");

        // save the book
        Book savedBook = bookRepository.save(book);
        logger.info("Book saved: {}", savedBook);
    }

    public void savePage() {
        Long bookID = 1L;
        // create a new book
        Optional<Book> optionalBook = bookRepository.findById(bookID);
        if (!optionalBook.isPresent()) {
            logger.error("Cannot find book with id {}", bookID);
            //return;
        }
        Book book = optionalBook.get();
        // create and save new pages
        pageRepository.save(new BookPage(1, "Introduction contents", "Introduction", book));
        pageRepository.save(new BookPage(2, "About author", "Introduction", book));
        pageRepository.save(new BookPage(65, "Java 8 contents", "Java 8", book));
    }

}
