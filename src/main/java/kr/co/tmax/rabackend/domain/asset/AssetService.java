package kr.co.tmax.rabackend.domain.asset;

import kr.co.tmax.rabackend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AssetService {
    private final AssetReader assetReader;

    public List<Asset> findAll() {
        return assetReader.findAll();
    }

    public Asset searchByTickerAndIndex(String ticker, String index) {
        return assetReader.searchByTickerAndIndex(ticker, index).orElseThrow(() -> new ResourceNotFoundException("자산 데이터가 존재하지 않습니다"));
    }
}