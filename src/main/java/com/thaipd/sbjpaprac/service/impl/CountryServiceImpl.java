package com.thaipd.sbjpaprac.service.impl;

import lombok.extern.slf4j.Slf4j;

import com.thaipd.sbjpaprac.dto.CountryDTO;
import com.thaipd.sbjpaprac.entity.Country;
import com.thaipd.sbjpaprac.entity.State;
import com.thaipd.sbjpaprac.mapper.CountryMapper;
import com.thaipd.sbjpaprac.repository.CountryRepository;
import com.thaipd.sbjpaprac.repository.StateRepository;
import com.thaipd.sbjpaprac.service.CountryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class CountryServiceImpl implements CountryService {

	CountryRepository countryRepository;
	StateRepository stateRepository;
	Validator validator;
	CountryMapper countryMapper;

	@Autowired
	public CountryServiceImpl(CountryRepository countryRepository, StateRepository stateRepository,
			Validator validator, CountryMapper countryMapper) {
		this.countryRepository = countryRepository;
		this.stateRepository = stateRepository;
		this.validator = validator;
		this.countryMapper = countryMapper;
	}

	@Transactional
	public CountryDTO getById(Long id) {
		log.debug("Fetching country by id: {}", id);
		CountryDTO response = null;
		Optional<Country> country = countryRepository.findById(id);

		if (country.isPresent()) {
			response = countryMapper.toDTO(country.get());
		} else {
			log.warn("Country not found with id: {}", id);
		}

		return response;
	}

	@Override
	public List<CountryDTO> getByCode(String code) {
		log.debug("Fetching countries by code: {}", code);
		List<Country> countries = countryRepository.findByCode(code);
		return countryMapper.toDTOList(countries);
	}

	public CountryDTO save(CountryDTO currency) {
		log.info("Saving country: {}", currency.getName());
		return saveInformation(currency);
	}

	public CountryDTO update(CountryDTO currency) {
		log.info("Updating country: {}", currency.getName());
		return saveInformation(currency);
	}

	@Transactional(readOnly = false, timeout = 10) // mặc định là false, ghi vào cho tường minh thôi
	public void delete(Long id) throws InterruptedException {
		log.info("Request to delete country with id: {}", id);
		Optional<Country> country = countryRepository.findById(id);
		List<State> states = stateRepository.findAllByCountry_CountryId(country.get().getCountryId());

		if (country.isPresent()) {
			log.info("Country found with id: {}", id);
			country.get().setEnabled(Boolean.FALSE);
			countryRepository.save(country.get());
			// Thread.sleep(2000L); // For the purposes of simulate different scenarios

			// update State to disabled
			for (State state : states) {
				state.setEnabled(Boolean.FALSE);
				stateRepository.save(state);
			}
		} else {
			log.info("Country not found with id: {}", id);
			throw new RuntimeException("Country not found");
		}
	}

	private CountryDTO saveInformation(CountryDTO country) {
		Country entity = countryMapper.toEntity(country);
		Country savedEntity = countryRepository.save(entity);

		Set<ConstraintViolation<Country>> violations = validator.validate(entity);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		return countryMapper.toDTO(savedEntity);
	}
}
