package com.thaipd.sbjpaprac.service.impl;

import lombok.extern.slf4j.Slf4j;

import com.thaipd.sbjpaprac.dto.CountryDTO;
import com.thaipd.sbjpaprac.entity.Country;
import com.thaipd.sbjpaprac.entity.State;
import com.thaipd.sbjpaprac.mapper.ApiMapper;
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

@Service
@Slf4j
public class CountryServiceImpl implements CountryService {

	CountryRepository countryRepository;
	StateRepository stateRepository;
	Validator validator;

	@Autowired
	public CountryServiceImpl(CountryRepository countryRepository, StateRepository stateRepository,
			Validator validator) {
		this.countryRepository = countryRepository;
		this.stateRepository = stateRepository;
		this.validator = validator;
	}

	public CountryDTO getById(Long id) {
		CountryDTO response = null;
		Optional<Country> country = countryRepository.findById(id);

		if (country.isPresent()) {
			response = ApiMapper.INSTANCE.entityToDTO(country.get());
		}

		return response;
	}

	@Override
	public List<CountryDTO> getByCode(String code) {
		List<Country> countries = countryRepository.findByCode(code);
		return ApiMapper.INSTANCE.entityToDTO(countries);
	}

	public CountryDTO save(CountryDTO currency) {
		return saveInformation(currency);
	}

	public CountryDTO update(CountryDTO currency) {
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
		Country entity = ApiMapper.INSTANCE.DTOToEntity(country);
		Country savedEntity = countryRepository.save(entity);

		Set<ConstraintViolation<Country>> violations = validator.validate(entity);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		return ApiMapper.INSTANCE.entityToDTO(savedEntity);
	}
}
