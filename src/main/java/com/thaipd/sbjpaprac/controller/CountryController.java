package com.thaipd.sbjpaprac.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.thaipd.sbjpaprac.dto.CountryDTO;
import com.thaipd.sbjpaprac.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/country")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<CountryDTO> getById(@PathVariable Long id) {
        log.debug("REST request to get Country : {}", id);
        CountryDTO response = countryService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/code/{code}")
    public ResponseEntity<List<CountryDTO>> getByCode(@PathVariable String code) {
        List<CountryDTO> response = countryService.getByCode(code);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CountryDTO> save(@RequestBody CountryDTO countryDTO) {
        log.info("REST request to save Country : {}", countryDTO.getName());
        CountryDTO response = countryService.save(countryDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CountryDTO> update(@RequestBody CountryDTO countryDTO) {
        CountryDTO response = countryService.update(countryDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws InterruptedException {
        log.warn("REST request to delete Country : {}", id);
        countryService.delete(id);
        return ResponseEntity.ok().build();
    }
}
