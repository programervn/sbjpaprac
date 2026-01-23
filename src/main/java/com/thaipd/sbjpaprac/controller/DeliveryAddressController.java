package com.thaipd.sbjpaprac.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thaipd.sbjpaprac.dto.DeliveryAddressDTO;
import com.thaipd.sbjpaprac.service.DeliveryAddressService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/delivery-address")
@RequiredArgsConstructor
public class DeliveryAddressController {

    private final DeliveryAddressService deliveryAddressService;

    @GetMapping("/{customerId}/{addressId}")
    public ResponseEntity<DeliveryAddressDTO> getDeliveryAddress(@PathVariable Long customerId,
            @PathVariable Long addressId) {
        log.debug("REST request to get DeliveryAddress for Customer : {} and Address : {}", customerId, addressId);
        return ResponseEntity.ok(deliveryAddressService.getDeliveryAddress(customerId, addressId));
    }
}
