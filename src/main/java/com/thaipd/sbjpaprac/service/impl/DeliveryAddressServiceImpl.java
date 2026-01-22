package com.thaipd.sbjpaprac.service.impl;

import org.springframework.stereotype.Service;

import com.thaipd.sbjpaprac.dto.DeliveryAddressDTO;
import com.thaipd.sbjpaprac.entity.Address;
import com.thaipd.sbjpaprac.entity.Customer;
import com.thaipd.sbjpaprac.mapper.DeliveryAddressMapper;
import com.thaipd.sbjpaprac.repository.AddressRepository;
import com.thaipd.sbjpaprac.repository.CustomerRepository;
import com.thaipd.sbjpaprac.service.DeliveryAddressService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final DeliveryAddressMapper deliveryAddressMapper;

    @Override
    public DeliveryAddressDTO getDeliveryAddress(Long customerId, Long addressId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        return deliveryAddressMapper.getDeliveryAddress(customer, address);
    }
}
