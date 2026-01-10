package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.dto.CountryDTO;
import com.thaipd.sbjpaprac.entity.Country;
import com.thaipd.sbjpaprac.mapper.ApiMapper;
import com.thaipd.sbjpaprac.repository.CountryRepository;
import com.thaipd.sbjpaprac.service.CountryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Service
public class CountryServiceImpl implements CountryService {

	CountryRepository repository;
	Validator validator;

	@Autowired
	public CountryServiceImpl(CountryRepository repository, Validator validator) {
		this.repository = repository;
		this.validator = validator;
	}

	public CountryDTO getById(Long id) {
		CountryDTO response = null;
		Optional<Country> country = repository.findById(id);

		if (country.isPresent()) {
			response = ApiMapper.INSTANCE.entityToDTO(country.get());
		}

		return response;
	}

	public CountryDTO save(CountryDTO currency) {
		return saveInformation(currency);
	}

	public CountryDTO update(CountryDTO currency) {
		return saveInformation(currency);
	}

	public void delete(Long id) {
		Optional<Country> country = repository.findById(id);

		if (country.isPresent()) {
			country.get().setEnabled(Boolean.FALSE);
			repository.save(country.get());
		}
	}

	private CountryDTO saveInformation(CountryDTO country) {
		Country entity = ApiMapper.INSTANCE.DTOToEntity(country);
		Country savedEntity = repository.save(entity);

		Set<ConstraintViolation<Country>> violations = validator.validate(entity);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		return ApiMapper.INSTANCE.entityToDTO(savedEntity);
	}
}
