package kr.co.tmax.rabackend.domain.trading;

import kr.co.tmax.rabackend.exception.BadRequestException;
import kr.co.tmax.rabackend.exception.ResourceNotFoundException;
import kr.co.tmax.rabackend.external.TradingEngineClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioStore portfolioStore;
    private final PortfolioReader portfolioReader;
    private final TradingEngineClient tradingEngineClient;
    private final PortfolioResultStore portfolioResultStore;
    private final PortfolioResultReader portfolioResultReader;

    public Portfolio save(final Portfolio portfolio) {
        Portfolio savedPortfolio = portfolioStore.save(portfolio);
        tradingEngineClient.doPostPortfolio(savedPortfolio);

        return savedPortfolio;
    }

    public void save(final PortfolioResult portfolioResult) {
        updateExecutedStatus(portfolioResult.getPortfolioId());

        var portfolioResults = portfolioResultReader.findAllByPortfolioId(portfolioResult.getPortfolioId());

        if (!portfolioResults.isEmpty()) {
            var prevPR = portfolioResults.get(0);
            portfolioResultStore.delete(prevPR);
        }

        portfolioResultStore.save(portfolioResult);
    }

    private void updateExecutedStatus(final String portfolioId) {
        Portfolio portfolio = portfolioReader.findById(portfolioId).orElseThrow(
                () -> new ResourceNotFoundException("Portfolio", "portfolioId", portfolioId)
        );

        portfolio.complete();

        portfolioStore.save(portfolio);
    }

    public Page<Portfolio> getAllByUserId(final String userId, Pageable pageable) {
        Page<Portfolio> allByUserId = portfolioReader.findAllByUserId(userId, pageable);

        allByUserId.getContent().stream()
                .parallel()
                .forEach(Portfolio::putPortfolioId);

        return allByUserId;
    }

    public Object getPortfolioResult(final String portfolioId, final int summaryNum) {
        var portfolioResult = portfolioResultReader.findByPortfolioId(portfolioId).orElseThrow(
                () -> new ResourceNotFoundException("Portfolio Result", "portfolioId", portfolioId)
        );

        switch (summaryNum){
            case 0:
                return portfolioResult.getSummary();
            case 1:
                return portfolioResult.getSummary1();
            case 2:
                return portfolioResult.getSummary2();
            default:
                throw new BadRequestException(MessageFormat.format("summary number {0} is not exists", summaryNum));
        }
    }
}
