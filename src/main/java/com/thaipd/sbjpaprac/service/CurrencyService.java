package com.thaipd.sbjpaprac.service;

import com.thaipd.sbjpaprac.dto.CurrencyDTO;

public interface CurrencyService {
	CurrencyDTO getById(Long id);

	CurrencyDTO save(CurrencyDTO currency);

	CurrencyDTO update(CurrencyDTO currency);

	void delete(Long id);
}
