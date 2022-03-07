package kr.co.tmax.rabackend.domain.asset;

import kr.co.tmax.rabackend.interfaces.asset.AssetDto;
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

    public Asset searchByTickerAndIndex(String ticker, String index) {
        return assetReader.searchByTickerAndIndex(ticker, index);
    }
}