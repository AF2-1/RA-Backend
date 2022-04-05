package kr.co.tmax.rabackend.domain.asset;

import kr.co.tmax.rabackend.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @InjectMocks
    AssetService assetService;

    @Mock
    AssetReader assetReader;

    @Test
    void 자산생성_테스트() {
        // given
        Asset asset = new Asset();

        // then
        assertThat(asset.getName()).isNull();
    }

    @Test
    void 자산빌더테스트() throws Exception {
        //given
        String saveAsset = Asset.builder()
                .id("1")
                .ticker("033780.KS")
                .index("KOSPI")
                .name("KT & G KT&G")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .toString();

        //then
        assertThat(String.class).isEqualTo(saveAsset.getClass());
     }

    @Test
    @DisplayName("자산데이터 조회 테스트")
    void 자산데이터_조회_테스트() {
        // given
        Asset saveAsset = Asset.builder()
                .id("1")
                .ticker("033780.KS")
                .index("KOSPI")
                .name("KT & G KT&G")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();
        given(assetReader.searchByTickerAndIndex(any(), any())).willReturn(Optional.ofNullable(saveAsset));

        // when
        Asset findAsset = assetService.searchByTickerAndIndex("033780.KS", "KOSPI");

        // then
        assertThat(findAsset.getName()).isEqualTo(saveAsset.getName());
        assertThat(findAsset.getTicker()).isEqualTo(saveAsset.getTicker());
        assertThat(findAsset.getIndex()).isEqualTo(saveAsset.getIndex());
        assertThat(findAsset.toString()).isEqualTo(saveAsset.toString());
    }

    @Test
    @DisplayName("없는 자산 데이터 조회시 ResourceNotFoundException 발생")
    void 없는자산인덱스_조회하면_ResourceNotFoundException발생() {
        // given
        given(assetReader.searchByTickerAndIndex("033780.KS", "KOS")).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> assetService.searchByTickerAndIndex("033780.KS", "KOS"));
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

        given(assetReader.findAll()).willReturn(ASSET_LIST);

        // when
        int findSize = assetService.findAll().size();

        // then
        assertThat(findSize).isEqualTo(ASSET_SIZE);
    }
}