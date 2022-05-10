package kr.co.tmax.rabackend.domain.trading;

import kr.co.tmax.rabackend.infrastructure.trading.PortfolioRepository;

public interface PortfolioStore extends PortfolioRepository {
    @Override
    Portfolio save(Portfolio portfolio);
}
