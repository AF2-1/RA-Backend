package kr.co.tmax.rabackend.domain.trading;

import kr.co.tmax.rabackend.exception.ResourceNotFoundException;
import kr.co.tmax.rabackend.external.TradingEngineClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioStore portfolioStore;
    private final PortfolioReader portfolioReader;
    private final TradingEngineClient tradingEngineClient;

    public Portfolio save(Portfolio portfolio) {
        Portfolio savedPortfolio = portfolioStore.save(portfolio);
        tradingEngineClient.requestPortfolioCreation(savedPortfolio);

        return savedPortfolio;
    }

    public void update(String portfolioId) {
        Portfolio portfolio = portfolioReader.findById(portfolioId).orElseThrow(
                () -> new ResourceNotFoundException("Portfolio", "portfolioId", portfolioId));

        portfolio.complete();
    }
}
