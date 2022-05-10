package kr.co.tmax.rabackend.domain.trading;

import kr.co.tmax.rabackend.external.TradingEngineClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioStore portfolioStore;
    private final TradingEngineClient tradingEngineClient;

    public Portfolio save(Portfolio portfolio) {
        Portfolio savedPortfolio = portfolioStore.save(portfolio);
        tradingEngineClient.requestPortfolioCreation(savedPortfolio);

        return savedPortfolio;
    }

}
