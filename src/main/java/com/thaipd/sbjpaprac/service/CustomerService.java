package com.thaipd.sbjpaprac.service;

import com.thaipd.sbjpaprac.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {

    CustomerDTO create(CustomerDTO customerDTO);

    CustomerDTO getById(Long id);

    List<CustomerDTO> getAll();

    CustomerDTO update(Long id, CustomerDTO customerDTO);

    void delete(Long id);
}
