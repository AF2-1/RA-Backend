package kr.co.tmax.rabackend.infrastructure.trading;

import kr.co.tmax.rabackend.domain.trading.Portfolio;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PortfolioRepository extends MongoRepository<Portfolio, ObjectId> {

    @Override
    <S extends Portfolio> S save(S entity);

    Page<Portfolio> findAllByUserId(String userId, Pageable pageable);

}
