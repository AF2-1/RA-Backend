package kr.co.tmax.rabackend.interfaces.trading;

import io.swagger.annotations.ApiOperation;
import kr.co.tmax.rabackend.config.common.CommonResponse;
import kr.co.tmax.rabackend.domain.trading.Portfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import java.net.URI;

@RequestMapping(value = "/api/v1/", produces = "application/json; charset=utf8")
@RequiredArgsConstructor
@RestController
public class TradingController {

    @ApiOperation(value = "포트폴리오 생성", notes = "포트폴리오를 생성합니다")
    @PostMapping("/users/{userId}/portfolios")
    public ResponseEntity<CommonResponse> registerPortfolio(@NotBlank @PathVariable String userId,
                                                            @RequestBody Portfolio portfolio,
                                                            UriComponentsBuilder uriComponentsBuilder) {
        System.out.println("portfolio = " + portfolio);
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
}
