package com.thaipd.sbjpaprac.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thaipd.sbjpaprac.dto.CurrencyDTO;
import com.thaipd.sbjpaprac.entity.Currency;
import com.thaipd.sbjpaprac.mapper.ApiMapper;
import com.thaipd.sbjpaprac.repository.CurrencyRepository;
import com.thaipd.sbjpaprac.service.CurrencyService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Service
public class CurrencyServiceImpl implements CurrencyService {

	CurrencyRepository repository;
	Validator validator;

	@Autowired
	public CurrencyServiceImpl(CurrencyRepository repository, Validator validator) {
		this.repository = repository;
		this.validator = validator;
	}

	public CurrencyDTO getById(Long id) {
		CurrencyDTO response = null;
		Optional<Currency> currency = repository.findById(id);

		if (currency.isPresent()) {
			response = ApiMapper.INSTANCE.entityToDTO(currency.get());
		}

		return response;
	}

	public CurrencyDTO save(CurrencyDTO currency) {
		return saveInformation(currency);
	}

	public CurrencyDTO update(CurrencyDTO currency) {
		return saveInformation(currency);
	}

	public void delete(Long id) {
		Optional<Currency> currency = repository.findById(id);

		if (currency.isPresent()) {
			currency.get().setEnabled(Boolean.FALSE);
			repository.save(currency.get());
		}
	}

	private CurrencyDTO saveInformation(CurrencyDTO currency) {
		Currency entity = ApiMapper.INSTANCE.DTOToEntity(currency);

		Set<ConstraintViolation<Currency>> violations = validator.validate(entity);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		Currency savedEntity = repository.save(entity);

		return ApiMapper.INSTANCE.entityToDTO(savedEntity);
	}
}
