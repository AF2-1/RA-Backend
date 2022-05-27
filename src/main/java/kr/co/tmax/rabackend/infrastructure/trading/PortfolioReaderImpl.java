package kr.co.tmax.rabackend.infrastructure.trading;

import kr.co.tmax.rabackend.domain.trading.Portfolio;
import kr.co.tmax.rabackend.domain.trading.PortfolioReader;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PortfolioReaderImpl implements PortfolioReader {
    private final PortfolioRepository portfolioRepository;

    @Override
    public Optional<Portfolio> findById(String portfolioId) {
        return portfolioRepository.findById(new ObjectId(portfolioId));
    }
}
