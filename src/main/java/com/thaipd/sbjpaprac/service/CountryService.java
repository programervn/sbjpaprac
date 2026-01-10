package com.thaipd.sbjpaprac.service;

import com.thaipd.sbjpaprac.dto.CountryDTO;

public interface CountryService {
	CountryDTO getById(Long id);

	CountryDTO save(CountryDTO country);

	CountryDTO update(CountryDTO country);

	void delete(Long id);
}
