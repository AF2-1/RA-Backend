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
    public Asset searchByTickerAndIndex(String ticker, String index){
        return assetRepository.searchByTickerAndIndex(ticker, index);
    }

    @Override
    public boolean existsByTickerAndIndex(String ticker, String index) {
        return assetRepository.existsByTickerAndIndex(ticker, index);
    }
}
