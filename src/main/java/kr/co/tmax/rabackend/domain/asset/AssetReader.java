package kr.co.tmax.rabackend.domain.asset;

import java.util.List;
import java.util.Optional;

public interface AssetReader {

    Optional<Asset> searchByTickerAndIndex(String ticker, String index);

    List<Asset> findAll();

    boolean existsByTickerAndIndex(String ticker, String index);
}
