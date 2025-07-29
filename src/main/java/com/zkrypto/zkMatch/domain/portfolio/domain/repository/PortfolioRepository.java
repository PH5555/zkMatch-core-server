package com.zkrypto.zkMatch.domain.portfolio.domain.repository;

import com.zkrypto.zkMatch.domain.portfolio.domain.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}
