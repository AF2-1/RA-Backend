package kr.co.tmax.rabackend.infrastructure.asset;

import kr.co.tmax.rabackend.domain.asset.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AssetRepository extends MongoRepository<Asset, String> {

    @Query("{ 'ticker' : { $regex: ?0, $options: 'i' }, 'index' : ?1 } }")
    Asset searchByTickerAndIndex(String ticker, String index);

    boolean existsByTickerAndIndex(String ticker, String index);
}
