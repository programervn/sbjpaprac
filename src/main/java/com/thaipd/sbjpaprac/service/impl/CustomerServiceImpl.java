package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.dto.CustomerDTO;
import com.thaipd.sbjpaprac.entity.Customer;
import com.thaipd.sbjpaprac.mapper.CustomerMapper;
import com.thaipd.sbjpaprac.repository.CustomerRepository;
import com.thaipd.sbjpaprac.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) {
        log.info("Creating customer: {}", customerDTO.firstName());
        Customer entity = customerMapper.toEntity(customerDTO);
        Customer saved = customerRepository.save(entity);
        log.info("Customer created with id: {}", saved.getCustomerId());
        return customerMapper.toDto(saved);
    }

    @Override
    public CustomerDTO getById(Long id) {
        log.debug("Fetching customer by id: {}", id);
        return customerRepository.findById(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Customer not found with id: {}", id);
                    return new RuntimeException("Customer not found with id: " + id);
                });
    }

    @Override
    public List<CustomerDTO> getAll() {
        // return
        // customerRepository.findAll().stream().map(customerMapper::toDto).toList();
        return customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO update(Long id, CustomerDTO customerDTO) {
        log.info("Updating customer with id: {}", id);
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with id: {}", id);
                    return new RuntimeException("Customer not found with id: " + id);
                });

        // Update fields
        customerMapper.updateEntityFromDto(customerDTO, existing);

        Customer updated = customerRepository.save(existing);
        log.info("Customer updated with id: {}", updated.getCustomerId());
        return customerMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        log.warn("Deleting customer with id: {}", id);
        if (!customerRepository.existsById(id)) {
            log.error("Customer not found with id: {}", id);
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
        log.info("Customer deleted with id: {}", id);
    }
}
