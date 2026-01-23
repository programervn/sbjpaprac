package com.thaipd.sbjpaprac.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thaipd.sbjpaprac.dto.CurrencyDTO;
import com.thaipd.sbjpaprac.entity.Currency;
import com.thaipd.sbjpaprac.mapper.CurrencyMapper;
import com.thaipd.sbjpaprac.repository.CurrencyRepository;
import com.thaipd.sbjpaprac.service.CurrencyService;

import lombok.extern.slf4j.Slf4j;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class CurrencyServiceImpl implements CurrencyService {

	CurrencyRepository repository;
	Validator validator;
	CurrencyMapper currencyMapper;

	@Autowired
	public CurrencyServiceImpl(CurrencyRepository repository, Validator validator, CurrencyMapper currencyMapper) {
		this.repository = repository;
		this.validator = validator;
		this.currencyMapper = currencyMapper;
	}

	public CurrencyDTO getById(Long id) {
		log.debug("Fetching currency by id: {}", id);
		CurrencyDTO response = null;
		Optional<Currency> currency = repository.findById(id);

		if (currency.isPresent()) {
			response = currencyMapper.toDTO(currency.get());
		} else {
			log.warn("Currency not found with id: {}", id);
		}

		return response;
	}

	public CurrencyDTO save(CurrencyDTO currency) {
		log.info("Saving currency: {}", currency.getCode());
		return saveInformation(currency);
	}

	public CurrencyDTO update(CurrencyDTO currency) {
		log.info("Updating currency: {}", currency.getCode());
		return saveInformation(currency);
	}

	public void delete(Long id) {
		log.warn("Deleting currency with id: {}", id);
		Optional<Currency> currency = repository.findById(id);

		if (currency.isPresent()) {
			currency.get().setEnabled(Boolean.FALSE);
			repository.save(currency.get());
			log.info("Currency deleted with id: {}", id);
		} else {
			log.error("Currency not found with id: {}", id);
		}
	}

	private CurrencyDTO saveInformation(CurrencyDTO currency) {
		Currency entity = currencyMapper.toEntity(currency);

		Set<ConstraintViolation<Currency>> violations = validator.validate(entity);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		Currency savedEntity = repository.save(entity);

		return currencyMapper.toDTO(savedEntity);
	}
}
