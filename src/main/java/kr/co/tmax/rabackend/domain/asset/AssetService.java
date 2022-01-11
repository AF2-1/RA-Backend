package kr.co.tmax.rabackend.domain.asset;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AssetService {

    private final AssetReader assetReader;

    public List<Asset> findAll() {
        return assetReader.findAll();
    }

    public Asset findAssetByTicker(String ticker){
        return assetReader.findAssetByTicker(ticker);
    }

    public List<Asset> findAssetByName(String name) {
        return assetReader.findAssetByName(name);
    }
}
