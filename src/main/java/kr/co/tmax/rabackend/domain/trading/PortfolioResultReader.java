package kr.co.tmax.rabackend.domain.trading;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface PortfolioResultReader {

    List<PortfolioResult> findAllByPortfolioId(@NotNull String portfolioId);

}
