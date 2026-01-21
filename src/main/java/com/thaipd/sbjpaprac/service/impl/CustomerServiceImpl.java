package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.dto.CustomerDTO;
import com.thaipd.sbjpaprac.entity.Customer;
import com.thaipd.sbjpaprac.mapper.CustomerMapper;
import com.thaipd.sbjpaprac.repository.CustomerRepository;
import com.thaipd.sbjpaprac.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) {
        Customer entity = customerMapper.toEntity(customerDTO);
        Customer saved = customerRepository.save(entity);
        return customerMapper.toDto(saved);
    }

    @Override
    public CustomerDTO getById(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
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
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));

        // Update fields
        customerMapper.updateEntityFromDto(customerDTO, existing);

        Customer updated = customerRepository.save(existing);
        return customerMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Car not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
}
