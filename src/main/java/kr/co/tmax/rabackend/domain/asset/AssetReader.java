package kr.co.tmax.rabackend.domain.asset;

import java.util.List;

public interface AssetReader {
    List<Asset> searchByTicker(String ticker);

    List<Asset> searchByName(String name);

    List<Asset> findAll();

    List<Asset> findByTickerIn(List<String> tickers);
}
