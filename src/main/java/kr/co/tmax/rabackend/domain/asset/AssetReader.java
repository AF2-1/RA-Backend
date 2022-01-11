package kr.co.tmax.rabackend.domain.asset;

import java.util.List;

public interface AssetReader {
    Asset findAssetByTicker(String ticker);

    List<Asset> findAssetByName(String name);

    List<Asset> findAll();
}
