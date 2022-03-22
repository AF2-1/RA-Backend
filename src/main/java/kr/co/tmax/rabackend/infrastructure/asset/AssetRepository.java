package kr.co.tmax.rabackend.infrastructure.asset;

import kr.co.tmax.rabackend.domain.asset.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface AssetRepository extends MongoRepository<Asset, String> {

    @Query("{ 'ticker' : ?0 , 'index' : ?1 } }")
    Optional<Asset> searchByTickerAndIndex(String ticker, String index);

    boolean existsByTickerAndIndex(String ticker, String index);
}
