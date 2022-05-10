package kr.co.tmax.rabackend.interfaces.trading;

import io.swagger.annotations.ApiOperation;
import kr.co.tmax.rabackend.config.common.CommonResponse;
import kr.co.tmax.rabackend.domain.trading.Portfolio;
import kr.co.tmax.rabackend.domain.trading.PortfolioService;
import kr.co.tmax.rabackend.external.TradingEngineClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import java.net.URI;

@Slf4j
@RequestMapping(value = "/api/v1/", produces = "application/json; charset=utf8")
@RequiredArgsConstructor
@RestController
@Validated
public class TradingController {

    private final TradingEngineClient tradingEngineClient;
    private final PortfolioService portfolioService;

    @ApiOperation(value = "포트폴리오 생성", notes = "포트폴리오를 생성합니다")
    @PostMapping("/users/{userId}/portfolios")
    public ResponseEntity<CommonResponse> registerPortfolio(@NotBlank @PathVariable String userId,
                                                            @RequestBody Portfolio portfolio,
                                                            UriComponentsBuilder uriComponentsBuilder) {

        Portfolio savedPortfolio = portfolioService.save(portfolio);
        tradingEngineClient.requestPortfolioCreation(savedPortfolio);

        log.debug("portfolio = {}", portfolio);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(getLocation(userId, uriComponentsBuilder))
                .body(CommonResponse.withMessage("포트폴리오 생성 요청 확인"));
    }

    private URI getLocation(String userId, UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder
                .path(String.format("/users/%s/simulations", userId))
                .build().toUri();
    }

    @ApiOperation(value = "포트폴리오 생성완료", notes = "포트폴리오를 생성완료에 대한 콜백 요청입니다.")
    @PostMapping("/portfolios/{portfolioId}/callback")
    public ResponseEntity<CommonResponse> completePortfolio(@NotBlank @PathVariable String portfolioId) {
        //TODO: aynch response implements

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.withMessage("Temporal Response"));
    }
}
