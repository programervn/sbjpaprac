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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final DeliveryAddressMapper deliveryAddressMapper;

    @Override
    public DeliveryAddressDTO getDeliveryAddress(Long customerId, Long addressId) {
        log.debug("Fetching delivery address for customerId: {} and addressId: {}", customerId, addressId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Customer not found with id: {}", customerId);
                    return new RuntimeException("Customer not found");
                });
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    log.error("Address not found with id: {}", addressId);
                    return new RuntimeException("Address not found");
                });

        return deliveryAddressMapper.getDeliveryAddress(customer, address);
    }
}
