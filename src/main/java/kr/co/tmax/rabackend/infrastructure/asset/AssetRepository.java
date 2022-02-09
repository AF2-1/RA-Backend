package kr.co.tmax.rabackend.infrastructure.asset;

import kr.co.tmax.rabackend.domain.asset.Asset;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AssetRepository extends MongoRepository<Asset, String> {

    Asset searchByTicker(String ticker);

    @Query("{ 'ticker' : { $regex: ?0, $options: 'i' } } }")
    List<Asset> searchBySuggestionTicker(String ticker);

    @Query("{ 'name' : { $regex: ?0, $options: 'i' } } }")
    List<Asset> searchByNameIgnoreCase(String name);

    List<Asset> findByTickerIn(List<String> tickers);

    boolean existsByTicker(String ticker);
}
