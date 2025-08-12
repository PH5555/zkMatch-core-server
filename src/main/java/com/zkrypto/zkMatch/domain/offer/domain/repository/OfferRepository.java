package com.zkrypto.zkMatch.domain.offer.domain.repository;

import com.zkrypto.zkMatch.domain.offer.domain.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {

}
