package com.thaipd.sbjpaprac.repository;

import com.thaipd.sbjpaprac.entity.Book;
import com.thaipd.sbjpaprac.entity.BookPage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookPageRepository extends JpaRepository<BookPage, Long> {

    List<BookPage> findByBook(Book book);
    List<BookPage> findByBook(Book book, Sort sort);
}
