package kr.co.tmax.rabackend.domain.trading;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PortfolioReader {

    Optional<Portfolio> findById(String portfolioId);

    Page<Portfolio> findAllByUserId(String userId, Pageable pageable);

}
