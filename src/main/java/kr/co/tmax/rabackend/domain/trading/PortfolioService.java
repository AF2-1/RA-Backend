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
    private final PortfolioResultStore portfolioResultStore;

    public Portfolio save(Portfolio portfolio) {
        Portfolio savedPortfolio = portfolioStore.save(portfolio);
        tradingEngineClient.doPostPortfolio(savedPortfolio);

        return savedPortfolio;
    }

    public void save(PortfolioResult portfolioResult) {
        updateExecutedStatus(portfolioResult.getPortfolioId());

        portfolioResultStore.save(portfolioResult);
    }

    private void updateExecutedStatus(final String portfolioId) {
        Portfolio portfolio = portfolioReader.findById(portfolioId).orElseThrow(
                () -> new ResourceNotFoundException("Portfolio", "portfolioId", portfolioId));

        portfolio.complete();

        portfolioStore.save(portfolio);
    }
}
