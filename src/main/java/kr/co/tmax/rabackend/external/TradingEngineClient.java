package kr.co.tmax.rabackend.external;

import kr.co.tmax.rabackend.domain.trading.Portfolio;

public interface TradingEngineClient {
    void requestPortfolioCreation(Portfolio portfolio, String userId);
}
