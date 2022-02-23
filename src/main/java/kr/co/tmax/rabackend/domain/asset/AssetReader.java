package kr.co.tmax.rabackend.domain.asset;

import java.util.List;

public interface AssetReader {

    List<Asset> searchByTickerAndIndex(String ticker, String index);

    List<Asset> searchByName(String name);

    List<Asset> findAll();

    List<Asset> findByTickerIn(List<String> tickers);

    Asset findByTickerAndIndex(String ticker, String index);

    boolean existsByTickerAndIndex(String ticker, String index);
}
