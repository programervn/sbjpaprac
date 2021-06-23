package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.entity.Library;
import com.thaipd.sbjpaprac.repository.LibraryBookRepository;
import com.thaipd.sbjpaprac.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/libraries")
public class LibraryController {
    private final LibraryRepository libraryRepository;
    private final LibraryBookRepository bookRepository;

    @Autowired
    public LibraryController(LibraryRepository libraryRepository, LibraryBookRepository bookRepository) {
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
    }

    @PostMapping
    public ResponseEntity<Library> create(@Valid @RequestBody Library library) {
        Library savedLibrary = libraryRepository.save(library);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedLibrary.getId()).toUri();

        return ResponseEntity.created(location).body(savedLibrary);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Library> update(@PathVariable Integer id, @Valid @RequestBody Library library) {
        Optional<Library> optionalLibrary = libraryRepository.findById(id);
        if (!optionalLibrary.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        library.setId(optionalLibrary.get().getId());
        libraryRepository.save(library);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Library> delete(@PathVariable Integer id) {
        Optional<Library> optionalLibrary = libraryRepository.findById(id);
        if (!optionalLibrary.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        deleteLibraryInTransaction(optionalLibrary.get());

        return ResponseEntity.noContent().build();
    }

    @Transactional
    public void deleteLibraryInTransaction(Library library) {
        bookRepository.deleteByLibraryId(library.getId());
        libraryRepository.delete(library);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Library> getById(@PathVariable Integer id) {
        Optional<Library> optionalLibrary = libraryRepository.findById(id);
        if (!optionalLibrary.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalLibrary.get());
    }

    @GetMapping
    public ResponseEntity<Page<Library>> getAllPaging(Pageable pageable) {
        return ResponseEntity.ok(libraryRepository.findAll(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Library>> getAll(Pageable pageable) {
        return ResponseEntity.ok(libraryRepository.findAll());
    }
}
