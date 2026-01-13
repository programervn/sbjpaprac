package com.thaipd.sbjpaprac.repository;

import com.thaipd.sbjpaprac.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Long> {
	List<State> findByCode(String code);

	List<State> findAllByCountry_CountryId(Long countryId);
}
