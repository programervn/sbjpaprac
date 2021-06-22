package com.thaipd.sbjpaprac.repository;

import com.thaipd.sbjpaprac.entity.LibraryBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LibraryBookRepository extends JpaRepository<LibraryBook, Integer> {
    List<LibraryBook> findByLibraryId(Integer libraryId);
    Page<LibraryBook> findByLibraryId(Integer libraryId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM LibraryBook b WHERE b.library.id = ?1")
    void deleteByLibraryId(Integer libraryId);
}
