package com.thaipd.sbjpaprac.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thaipd.sbjpaprac.entity.Country;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {
	List<Country> findByCode(String code);
}
