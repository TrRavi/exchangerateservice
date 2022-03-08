package com.exchangerateservice.repository;

import com.exchangerateservice.entity.CurrencyReferenceRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyReferenceRateRepository extends JpaRepository<CurrencyReferenceRate,Long> {
   CurrencyReferenceRate findByCurrencyCode(String currencyCode);
}
