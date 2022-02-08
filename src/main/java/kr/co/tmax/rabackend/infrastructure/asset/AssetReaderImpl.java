package kr.co.tmax.rabackend.infrastructure.asset;

import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetReader;
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
    public List<Asset> searchByName(String name) {
        return assetRepository.searchByName(name);
    }

    @Override
    public List<Asset> searchBySuggestionTicker(String ticker) {
        return assetRepository.searchBySuggestionTicker(ticker);
    }

    @Override
    public Asset searchByTicker(String ticker) {
        return assetRepository.searchByTicker(ticker);
    }

    @Override
    public List<Asset> findByTickerIn(List<String> tickers) {
        return assetRepository.findByTickerIn(tickers);
    }
}
