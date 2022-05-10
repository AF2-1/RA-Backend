package kr.co.tmax.rabackend.infrastructure.trading;

import kr.co.tmax.rabackend.domain.trading.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PortfolioRepository extends MongoRepository<Portfolio, String> {

    @Override
    <S extends Portfolio> S save(S entity);

}
