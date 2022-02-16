package kr.co.tmax.rabackend.infrastructure.asset;

import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetReader;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AssetRepository extends MongoRepository<Asset, String> {

    @Query("{ 'ticker' : { $regex: ?0, $options: 'i' } } }")
    List<Asset> searchByTicker(String ticker);

    @Query("{ 'name' : { $regex: ?0, $options: 'i' } } }")
    List<Asset> searchByName(String name);

    Asset findByTicker(String ticker);

    List<Asset> findByTickerIn(List<String> tickers);

    boolean existsByTicker(String ticker);
}
