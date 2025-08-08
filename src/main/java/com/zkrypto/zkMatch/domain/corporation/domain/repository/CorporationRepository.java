package com.zkrypto.zkMatch.domain.corporation.domain.repository;

import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CorporationRepository extends JpaRepository<Corporation, UUID> {
    boolean existsCorporationByRegisterNumber(String registerNumber);

    boolean existsCorporationByCorporationName(String corporationName);
}
