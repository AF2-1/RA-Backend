package kr.co.tmax.rabackend.infrastructure.trading;

import kr.co.tmax.rabackend.domain.trading.Portfolio;
import kr.co.tmax.rabackend.domain.trading.PortfolioStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PortfolioStoreImpl implements PortfolioStore {
    private final PortfolioRepository portfolioRepository;

    @Override
    public Portfolio save(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }
}
