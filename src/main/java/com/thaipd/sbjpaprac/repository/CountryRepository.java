package com.thaipd.sbjpaprac.repository;

import org.springframework.data.repository.CrudRepository;

import com.thaipd.sbjpaprac.entity.Country;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Long> {
	List<Country> findByCode(String code);
}
