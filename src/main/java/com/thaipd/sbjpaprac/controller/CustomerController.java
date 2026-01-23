package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.dto.CustomerDTO;
import com.thaipd.sbjpaprac.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> create(@RequestBody CustomerDTO customerDTO) {
        log.info("REST request to save Customer : {}", customerDTO.firstName());
        CustomerDTO created = customerService.create(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Long id) {
        CustomerDTO customer = customerService.getById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping({ "", "/" })
    public ResponseEntity<List<CustomerDTO>> getAll() {
        List<CustomerDTO> customers = customerService.getAll();
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updated = customerService.update(id, customerDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.warn("REST request to delete Customer : {}", id);
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
