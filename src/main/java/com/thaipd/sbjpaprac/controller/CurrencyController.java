package com.thaipd.sbjpaprac.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thaipd.sbjpaprac.dto.CurrencyDTO;
import com.thaipd.sbjpaprac.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<CurrencyDTO> getById(@PathVariable Long id) {
        log.debug("REST request to get Currency : {}", id);
        CurrencyDTO response = currencyService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CurrencyDTO> save(@RequestBody CurrencyDTO currencyDTO) {
        log.info("REST request to save Currency : {}", currencyDTO.getCode());
        CurrencyDTO response = currencyService.save(currencyDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CurrencyDTO> update(@RequestBody CurrencyDTO currencyDTO) {
        CurrencyDTO response = currencyService.update(currencyDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.warn("REST request to delete Currency : {}", id);
        currencyService.delete(id);
        return ResponseEntity.ok().build();
    }
}
