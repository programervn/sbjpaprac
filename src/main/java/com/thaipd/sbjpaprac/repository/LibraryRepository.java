package com.thaipd.sbjpaprac.repository;

import com.thaipd.sbjpaprac.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Integer>{
}
