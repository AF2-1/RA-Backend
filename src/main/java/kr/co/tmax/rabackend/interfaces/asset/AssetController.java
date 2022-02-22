package kr.co.tmax.rabackend.interfaces.asset;

import io.swagger.annotations.ApiOperation;
import kr.co.tmax.rabackend.common.CommonResponse;
import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@RestController
public class AssetController {

    private final AssetService assetService;
    private final ModelMapper modelMapper;

    @ApiOperation(value = "자산군 목록 조회", notes = "자산군 목록을 조회합니다")
    @GetMapping("/assets")
    public CommonResponse findAsset(@RequestParam(required = false) String ticker,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String index) {
        log.info("ticker:{} name:{}", ticker, name);

        if (StringUtils.hasText(name)) {
            List<AssetDto> result = assetService.searchByName(name)
                    .stream().map(AssetDto::new).collect(Collectors.toList());
            return CommonResponse.withMessageAndData("이름으로 자산 조회 성공", result);
        }

        if (StringUtils.hasText(ticker)) {
            List<AssetDto> result = assetService.searchByTickerAndIndex(ticker, index)
                    .stream().map(AssetDto::new).collect(Collectors.toList());
            return CommonResponse.withMessageAndData("티커와 인덱스로 자산 조회 성공", result);
        }

        List<AssetDto> result = assetService.findAll()
                .stream().map(AssetDto::new).collect(Collectors.toList());

        return CommonResponse.withMessageAndData("전체 자산 조회 성공", result);
    }
}
