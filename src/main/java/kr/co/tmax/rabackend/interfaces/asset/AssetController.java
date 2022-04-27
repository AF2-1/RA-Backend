package kr.co.tmax.rabackend.interfaces.asset;

import io.swagger.annotations.ApiOperation;
import kr.co.tmax.rabackend.config.common.CommonResponse;
import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@RestController
public class AssetController {

    private final AssetService assetService;
    private final ModelMapper modelMapper;

    @Cacheable(value = "assets", cacheManager = "assetCacheManager")
    @ApiOperation(value = "자산군 목록 조회", notes = "자산군 목록을 조회합니다")
    @GetMapping("/assets")
    public CommonResponse findAsset() {
//
//        if (StringUtils.hasText(ticker) && StringUtils.hasText(index)) {
//            Asset asset = assetService.searchByTickerAndIndex(ticker, index);
//            AssetDto result = modelMapper.map(asset, AssetDto.class);
//            return CommonResponse.withMessageAndData("티커와 인덱스로 자산 조회 성공", result);
//        }
        List<AssetDto> result = assetService.findAll()
                .stream().map(AssetDto::new).collect(Collectors.toList());
        return CommonResponse.withMessageAndData("전체 자산 조회 성공", result);
    }
}
