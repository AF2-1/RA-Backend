package kr.co.tmax.rabackend.interfaces.asset;

import kr.co.tmax.rabackend.common.CommonResponse;
import kr.co.tmax.rabackend.domain.asset.AssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/assets")
    public CommonResponse findAsset(@RequestParam(required = false) String ticker,
                                    @RequestParam(required = false) String name) {
        log.info("ticker:{} name:{}", ticker, name);

        if (StringUtils.hasText(name)) {
            List<AssetDto> result = assetService.searchByName(name)
                    .stream().map(AssetDto::new).collect(Collectors.toList());
            return CommonResponse.withMessageAndData("이름으로 자산 조회 성공", result);
        }

        if (StringUtils.hasText(ticker)) {
            List<AssetDto> result = assetService.searchByTicker(ticker)
                    .stream().map(AssetDto::new).collect(Collectors.toList());
            return CommonResponse.withMessageAndData("티커로 자산 조회 성공", result);
        }

        List<AssetDto> result = assetService.findAll()
                .stream().map(AssetDto::new).collect(Collectors.toList());

        return CommonResponse.withMessageAndData("전체 자산 조회 성공", result);
    }
}
