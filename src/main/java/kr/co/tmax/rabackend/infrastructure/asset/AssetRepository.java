package kr.co.tmax.rabackend.infrastructure.asset;

import kr.co.tmax.rabackend.domain.asset.Asset;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AssetRepository extends CrudRepository<Asset, String> {

    Optional<Asset> findByTickerAndIndex(String ticker, String index);

    boolean existsByTickerAndIndex(String ticker, String index);
}
