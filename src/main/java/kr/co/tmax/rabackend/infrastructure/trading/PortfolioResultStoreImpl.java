package kr.co.tmax.rabackend.infrastructure.trading;

import kr.co.tmax.rabackend.domain.trading.PortfolioResult;
import kr.co.tmax.rabackend.domain.trading.PortfolioResultStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PortfolioResultStoreImpl implements PortfolioResultStore {
    private final PortfolioResultRepository portfolioResultRepository;

    @Override
    public PortfolioResult save(PortfolioResult portfolioResult) {
        return portfolioResultRepository.save(portfolioResult);
    }

    @Override
    public void delete(PortfolioResult entity) {
        portfolioResultRepository.delete(entity);
    }
}
