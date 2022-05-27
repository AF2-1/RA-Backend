package kr.co.tmax.rabackend.infrastructure.trading;

import kr.co.tmax.rabackend.domain.trading.PortfolioResult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PortfolioResultRepository extends MongoRepository<PortfolioResult, ObjectId> {
    @Override
    <S extends PortfolioResult> S save(S entity);
}
