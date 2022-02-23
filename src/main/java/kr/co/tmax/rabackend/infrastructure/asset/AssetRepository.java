package kr.co.tmax.rabackend.infrastructure.asset;

import kr.co.tmax.rabackend.domain.asset.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AssetRepository extends MongoRepository<Asset, String> {

    @Query("{ 'ticker' : { $regex: ?0, $options: 'i' }, 'index' : ?1 } }")
    List<Asset> searchByTickerAndIndex(String ticker, String index);

    @Query("{ 'name' : { $regex: ?0, $options: 'i' } } }")
    List<Asset> searchByName(String name);

    Asset findByTickerAndIndex(String ticker, String index);

    List<Asset> findByTickerIn(List<String> tickers);

    boolean existsByTickerAndIndex(String ticker, String index);
}
