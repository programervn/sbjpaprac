package com.thaipd.sbjpaprac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thaipd.sbjpaprac.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
