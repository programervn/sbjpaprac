package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.entity.BookPage;
import com.thaipd.sbjpaprac.exception.ResourceNotFoundException;
import com.thaipd.sbjpaprac.repository.BookPageRepository;
import com.thaipd.sbjpaprac.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookpages")
public class BookPageController {
    private static final Logger logger = LoggerFactory.getLogger(BookPageController.class);
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookPageRepository bookPageRepository;

    @GetMapping("/{bookId}/pages/{pageId}")
    public BookPage getCommentById(@PathVariable(value = "bookId") Long bookId,
                                   @PathVariable (value = "pageId") Long pageId) {
        logger.debug("Get Page by: bookId={}, pageId={}", bookId, pageId);
        return bookPageRepository.findById(pageId).orElseThrow(() -> new ResourceNotFoundException("Cannot find page id " + pageId));
    }
}
