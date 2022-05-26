package kr.co.tmax.rabackend.interfaces.info;

import io.swagger.annotations.ApiOperation;
import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.config.common.CommonResponse;
import kr.co.tmax.rabackend.exception.ExternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RequestMapping(value = "/api/v1/")
@RestController
@RequiredArgsConstructor
public class InfoController {

    private final WebClient webClient;
    private final AppProperties appProperties;

    @ApiOperation(value = "키워드 추출 생성", notes = "키워드 추출을 생성합니다")
    @PostMapping("/keywords")
    public ResponseEntity<CommonResponse> registerPortfolio(@RequestBody InfoDto.KeywordRequest keywordRequest) {
        String url = appProperties.getInfo().getUrl();

        var response = webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(keywordRequest)
                .retrieve()
                .toEntity(Object.class)
                .onErrorMap(e -> {
                    throw new ExternalException(e.getMessage());
                })
                .block();

        if(!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.withMessage("외부 모듈 에러"));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.withMessageAndData("키워드 추출 생성 요청 확인", response.getBody()));
    }

}
