package kr.co.tmax.rabackend.domain.asset;

import java.util.List;

public interface AssetReader {

    Asset searchByTickerAndIndex(String ticker, String index);

    List<Asset> findAll();

    boolean existsByTickerAndIndex(String ticker, String index);
}
