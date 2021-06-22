package com.thaipd.sbjpaprac.controller;
import com.thaipd.sbjpaprac.entity.Library;
import com.thaipd.sbjpaprac.entity.LibraryBook;
import com.thaipd.sbjpaprac.repository.LibraryBookRepository;
import com.thaipd.sbjpaprac.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/librariebooks")
public class LibraryBookController {
    private final LibraryBookRepository libraryBookRepository;
    private final LibraryRepository libraryRepository;

    @Autowired
    public LibraryBookController(LibraryBookRepository libraryBookRepository, LibraryRepository libraryRepository) {
        this.libraryBookRepository = libraryBookRepository;
        this.libraryRepository = libraryRepository;
    }

    @PostMapping
    public ResponseEntity<LibraryBook> create(@RequestBody @Valid LibraryBook book) {
        Optional<Library> optionalLibrary = libraryRepository.findById(book.getLibrary().getId());
        if (!optionalLibrary.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        book.setLibrary(optionalLibrary.get());

        LibraryBook savedBook = libraryBookRepository.save(book);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedBook.getId()).toUri();

        return ResponseEntity.created(location).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibraryBook> update(@RequestBody @Valid LibraryBook book, @PathVariable Integer id) {
        Optional<Library> optionalLibrary = libraryRepository.findById(book.getLibrary().getId());
        if (!optionalLibrary.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Optional<LibraryBook> optionalBook = libraryBookRepository.findById(id);
        if (!optionalBook.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        book.setLibrary(optionalLibrary.get());
        book.setId(optionalBook.get().getId());
        libraryBookRepository.save(book);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LibraryBook> delete(@PathVariable Integer id) {
        Optional<LibraryBook> optionalBook = libraryBookRepository.findById(id);
        if (!optionalBook.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        libraryBookRepository.delete(optionalBook.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<LibraryBook>> getAll(Pageable pageable) {
        return ResponseEntity.ok(libraryBookRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibraryBook> getById(@PathVariable Integer id) {
        Optional<LibraryBook> optionalBook = libraryBookRepository.findById(id);
        if (!optionalBook.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalBook.get());
    }

    @GetMapping("/library/{libraryId}")
    public ResponseEntity<Page<LibraryBook>> getByLibraryIdPaging(@PathVariable Integer libraryId, Pageable pageable) {
        return ResponseEntity.ok(libraryBookRepository.findByLibraryId(libraryId, pageable));
    }

    @GetMapping("/library/{libraryId}/all")
    public ResponseEntity<List<LibraryBook>> getByLibraryId(@PathVariable Integer libraryId, Pageable pageable) {
        return ResponseEntity.ok(libraryBookRepository.findByLibraryId(libraryId));
    }
}
