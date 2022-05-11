package kr.co.tmax.rabackend.domain.trading;

import java.util.Optional;

public interface PortfolioReader {

    Optional<Portfolio> findById(String portfolioId);

}
