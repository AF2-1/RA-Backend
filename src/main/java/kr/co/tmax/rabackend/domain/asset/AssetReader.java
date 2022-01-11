package kr.co.tmax.rabackend.domain.asset;

import java.util.List;

public interface AssetReader {
    List<Asset> findByTicker(String ticker);

    List<Asset> findByName(String name);

    List<Asset> findAll();
}
