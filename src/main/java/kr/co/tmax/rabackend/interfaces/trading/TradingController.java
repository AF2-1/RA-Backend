package kr.co.tmax.rabackend.interfaces.trading;

import io.swagger.annotations.ApiOperation;
import kr.co.tmax.rabackend.config.common.CommonResponse;
import kr.co.tmax.rabackend.config.common.PageResponseDate;
import kr.co.tmax.rabackend.domain.trading.Portfolio;
import kr.co.tmax.rabackend.domain.trading.PortfolioResult;
import kr.co.tmax.rabackend.domain.trading.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;

@RequestMapping(value = "/api/v1/", produces = "application/json; charset=utf8")
@RequiredArgsConstructor
@RestController
@Validated
public class TradingController {

    private final PortfolioService portfolioService;

    @ApiOperation(value = "포트폴리오 생성", notes = "포트폴리오를 생성합니다")
    @PostMapping("/users/{userId}/portfolios")
    public ResponseEntity<CommonResponse> registerPortfolio(@NotBlank @PathVariable String userId,
                                                            @Valid @RequestBody Portfolio portfolio,
                                                            UriComponentsBuilder uriComponentsBuilder) {

        portfolio.setInitialValue(LocalDateTime.now(), false, userId);
        var savedPortfolio = portfolioService.save(portfolio);

        var response = new HashMap<String, String>();
        response.put("portfolioId", savedPortfolio.getId().toHexString());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(getLocation(userId, uriComponentsBuilder))
                .body(CommonResponse.withMessageAndData("포트폴리오 생성 요청 확인", response));
    }

    private URI getLocation(String userId, UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder
                .path(String.format("/users/%s/portfolios", userId))
                .build().toUri();
    }

    @ApiOperation(value = "유저별 포트폴리오 목록조회")
    @GetMapping("/users/{userId}/portfolios")
    public ResponseEntity<CommonResponse> getPortfoliosByUser(
            @NotBlank @PathVariable String userId,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC, size = 20) Pageable pageable) {

        Page<Portfolio> portfoliosPage = portfolioService.getAllByUserId(userId, pageable);

        var response = PageResponseDate.of(portfoliosPage.getContent(), PageResponseDate.PageInfo.of(portfoliosPage));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.withMessageAndData("포트폴리오 목록 조회 성공", response));
    }

    @ApiOperation(value = "포트폴리오 생성완료", notes = "엔진으로부터 포트폴리오 생성완료에 대한 콜백 요청입니다. \n 포트폴리오 분석 결과 데이터가 Body에 포함됩니다.")
    @PostMapping("/portfolios/{portfolioId}/callback")
    public ResponseEntity<CommonResponse> completePortfolio(
            @NotBlank @PathVariable String portfolioId,
            @Valid @RequestBody PortfolioResult request) {

        if (Boolean.FALSE.equals(request.getIsSuccess()))
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CommonResponse.withMessage("포트폴리오 생성 중 문제발생 확인"));

        portfolioService.save(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.withMessage("포트폴리오 생성/갱신 성공"));
    }

    @ApiOperation(value = "포트폴리오 분석 결과 조회 (Summary 0, 1, 2를 지원합니다)")
    @GetMapping("/portfolios/{portfolioId}/results")
    public ResponseEntity<CommonResponse> getPortfolioResult(
            @RequestParam(value = "summary") @Min(0) @Max(2) int summaryNum,
            @NotBlank @PathVariable String portfolioId
    ) {

        var portfolioResult = portfolioService.getPortfolioResult(portfolioId, summaryNum);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.withMessageAndData("포트폴리오 분석 결과 조회 성공", portfolioResult));
    }
}
