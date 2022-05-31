package kr.co.tmax.rabackend.infrastructure.trading;

import kr.co.tmax.rabackend.domain.trading.PortfolioResult;
import kr.co.tmax.rabackend.domain.trading.PortfolioResultReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@Component
public class PortfolioResultReaderImpl implements PortfolioResultReader {
    private final PortfolioResultRepository portfolioResultRepository;

    @Override
    public List<PortfolioResult> findAllByPortfolioId(@NotNull final String portfolioId) {
        return portfolioResultRepository.findAllByPortfolioId(portfolioId);
    }

}
