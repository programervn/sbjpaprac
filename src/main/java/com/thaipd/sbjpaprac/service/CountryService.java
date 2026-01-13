package com.thaipd.sbjpaprac.service;

import java.util.List;

import com.thaipd.sbjpaprac.dto.CountryDTO;

public interface CountryService {
	CountryDTO getById(Long id);

	List<CountryDTO> getByCode(String code);

	CountryDTO save(CountryDTO country);

	CountryDTO update(CountryDTO country);

	void delete(Long id) throws InterruptedException;
}
