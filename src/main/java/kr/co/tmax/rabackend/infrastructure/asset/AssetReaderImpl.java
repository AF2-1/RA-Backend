package kr.co.tmax.rabackend.infrastructure.asset;

import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetReader;
import kr.co.tmax.rabackend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AssetReaderImpl implements AssetReader {

    private final AssetRepository assetRepository;

    @Override
    public List<Asset> findAll() {
        return assetRepository.findAll();
    }

    @Override
    public List<Asset> findAssetByName(String name) {
        return assetRepository.findByName(name);
    }

    @Override
    public Asset findAssetByTicker(String ticker) {
       return assetRepository.findByTicker(ticker)
                .orElseThrow(() -> new ResourceNotFoundException("asset", "ticker", ticker));
    }
}
