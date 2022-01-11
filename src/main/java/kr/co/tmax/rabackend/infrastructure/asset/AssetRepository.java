package kr.co.tmax.rabackend.infrastructure.asset;

import kr.co.tmax.rabackend.domain.asset.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends MongoRepository<Asset, String> {
    Optional<Asset> findByTicker(String ticker);

    @Query("{ 'name' : { $regex: ?0, $options: 'i' } } }")
    List<Asset> findByName(String name);
}
