package com.thaipd.sbjpaprac.service;

import com.thaipd.sbjpaprac.dto.DeliveryAddressDTO;

public interface DeliveryAddressService {
    DeliveryAddressDTO getDeliveryAddress(Long customerId, Long addressId);
}
