package kr.co.tmax.rabackend.interfaces.trading;

import io.swagger.annotations.ApiOperation;
import kr.co.tmax.rabackend.domain.trading.Portfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RequestMapping(value = "/api/v1/", produces = "application/json; charset=utf8")
@RequiredArgsConstructor
@RestController
public class TradingController {

    @ApiOperation(value = "포트폴리오 생성", notes = "포트폴리오를 생성합니다")
    @PostMapping("/users/{userId}/portfolios")
    public Portfolio registerPortfolio(@NotBlank @PathVariable String userId, @RequestBody Portfolio portfolio) {
        System.out.println("portfolio = " + portfolio);
        return portfolio;
    }
}
