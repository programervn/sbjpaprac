package com.thaipd.sbjpaprac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thaipd.sbjpaprac.entity.Currency;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
	// General queries
	List<Currency> findByCode(String code);

	List<Currency> findByCodeAndDescription(String code, String description);

	// Order queries
	List<Currency> findByDescriptionOrderByCodeAsc(String description);

	List<Currency> findByDescriptionOrderByCodeDesc(String description);

	// Manual query
	@Query("SELECT c FROM Currency c where c.code = :code")
	Currency retrieveByCode(@Param("code") String code);
}
