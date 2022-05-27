package kr.co.tmax.rabackend.interfaces.trading;

import io.swagger.annotations.ApiOperation;
import kr.co.tmax.rabackend.config.common.CommonResponse;
import kr.co.tmax.rabackend.domain.trading.Portfolio;
import kr.co.tmax.rabackend.domain.trading.PortfolioCommand;
import kr.co.tmax.rabackend.domain.trading.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import java.net.URI;

@RequestMapping(value = "/api/v1/", produces = "application/json; charset=utf8")
@RequiredArgsConstructor
@RestController
@Validated
public class TradingController {

    private final PortfolioService portfolioService;

    @ApiOperation(value = "포트폴리오 생성", notes = "포트폴리오를 생성합니다")
    @PostMapping("/users/{userId}/portfolios")
    public ResponseEntity<CommonResponse> registerPortfolio(@NotBlank @PathVariable String userId,
                                                            @RequestBody Portfolio portfolio,
                                                            UriComponentsBuilder uriComponentsBuilder) {

        portfolioService.save(portfolio);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(getLocation(userId, uriComponentsBuilder))
                .body(CommonResponse.withMessage("포트폴리오 생성 요청 확인"));
    }

    private URI getLocation(String userId, UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder
                .path(String.format("/users/%s/portfolios", userId))
                .build().toUri();
    }

    @ApiOperation(value = "포트폴리오 생성완료", notes = "엔진으로부터 포트폴리오 생성완료에 대한 콜백 요청입니다. \n 포트폴리오 분석 결과 데이터가 Body에 포함됩니다.")
    @PostMapping("/portfolios/{portfolioId}/callback")
    public ResponseEntity<CommonResponse> completePortfolio(
            @NotBlank @PathVariable String portfolioId,
            @RequestBody TradingDto.RegisterPortfolioResultRequest request) {

        if(Boolean.FALSE.equals(request.getIsSuccess()))
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CommonResponse.withMessage("포트폴리오 생성 중 문제발생 확인"));

        portfolioService.save(new PortfolioCommand.RegisterPortfolioCallbackRequest(portfolioId));

        // TODO: 예외처리
        // TODO: resultUrl 프론트에 전달

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.withMessage("포트폴리오 생성 성공"));
    }
}
