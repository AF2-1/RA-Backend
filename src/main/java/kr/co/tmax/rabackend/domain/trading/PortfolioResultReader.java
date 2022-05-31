package kr.co.tmax.rabackend.domain.trading;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface PortfolioResultReader {

    List<PortfolioResult> findAllByPortfolioId(@NotNull String portfolioId);

    Optional<PortfolioResult> findByPortfolioId(String portfolioId);

}
