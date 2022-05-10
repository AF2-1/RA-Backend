package kr.co.tmax.rabackend.domain.trading;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioStore portfolioStore;

    public Portfolio save(Portfolio portfolio) {
        return portfolioStore.save(portfolio);
    }

}
