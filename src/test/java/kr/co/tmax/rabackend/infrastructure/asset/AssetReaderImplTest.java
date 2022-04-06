package kr.co.tmax.rabackend.infrastructure.asset;

import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AssetReaderImplTest {

    @Mock
    AssetRepository assetRepository;

    @InjectMocks
    AssetReaderImpl assetReaderImpl;

    @Test
    void 자산리더임플_동작_테스트() {

        // given
        Asset saveAsset = Asset.builder()
                .id("1")
                .ticker("033780.KS")
                .index("KOSPI")
                .name("KT & G KT&G")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();
        given(assetRepository.findByTickerAndIndex(any(), any())).willReturn(Optional.ofNullable(saveAsset));
        given(assetRepository.existsByTickerAndIndex(any(), any())).willReturn(true);

        // when
        Asset findAsset = assetReaderImpl.searchByTickerAndIndex("033780.KS", "KOSPI").orElse(new Asset());
        Boolean isExist = assetReaderImpl.existsByTickerAndIndex("033780.KS", "KOSPI");

        // then
        assertThat(findAsset.getName()).isEqualTo(saveAsset.getName());
        assertThat(findAsset.getTicker()).isEqualTo(saveAsset.getTicker());
        assertThat(findAsset.getIndex()).isEqualTo(saveAsset.getIndex());
        assertThat(findAsset.toString()).isEqualTo(saveAsset.toString());
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("자산3개 저장시 전체 자산 조회하면 3개의 자산 데이터 사이즈")
    void 자산3개_저장시_전체자산_조회하면_3개의_자산데이터() {
        // given
        int ASSET_SIZE = 3;
        List<Asset> ASSET_LIST = new ArrayList<>();

        Asset asset1 = Asset.builder()
                .id("1")
                .build();

        Asset asset2 = Asset.builder()
                .id("2")
                .build();

        Asset asset3 = Asset.builder()
                .id("3")
                .build();

        ASSET_LIST.add(asset1);
        ASSET_LIST.add(asset2);
        ASSET_LIST.add(asset3);

        given(assetRepository.findAll()).willReturn(ASSET_LIST);

        // when
        int findSize = assetReaderImpl.findAll().size();

        // then
        assertThat(findSize).isEqualTo(ASSET_SIZE);
    }
}