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
        tradingEngineClient.doPostPortfolio(savedPortfolio);

        return savedPortfolio;
    }

    public void save(PortfolioCommand.RegisterPortfolioCallbackRequest command) {
        Portfolio portfolio = portfolioReader.findById(command.getPortfolioId()).orElseThrow(
                () -> new ResourceNotFoundException("Portfolio", "portfolioId", command.getPortfolioId()));

        portfolio.complete();

        portfolioStore.save(portfolio);
    }
}
