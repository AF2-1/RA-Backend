package kr.co.tmax.rabackend.interfaces.info;

import io.swagger.annotations.ApiOperation;
import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.config.common.CommonResponse;
import kr.co.tmax.rabackend.exception.ExternalException;
import kr.co.tmax.rabackend.external.InfoEngineClientImpl;
import kr.co.tmax.rabackend.interfaces.info.InfoDto.KeywordRequest;
import kr.co.tmax.rabackend.interfaces.info.InfoDto.SentenceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RequestMapping(value = "/api/v1/")
@RestController
@RequiredArgsConstructor
public class InfoController {

    private final WebClient webClient;
    private final AppProperties appProperties;
    private final InfoEngineClientImpl infoEngineClient;

    @ApiOperation(value = "키워드 추출 생성", notes = "키워드 추출을 생성합니다")
    @Cacheable(value = "keywords", cacheManager = "cacheManager")
    @PostMapping("/keywords")
    public CommonResponse findKeywords(@RequestBody KeywordRequest keywordRequest) {

        ResponseEntity<Object> response = infoEngineClient.getKeywords(keywordRequest);

        if(!response.getStatusCode().is2xxSuccessful()) {
            return CommonResponse.withMessage("외부 모듈 에러");
        }

        return CommonResponse.withMessageAndData("키워드 추출 생성 요청 확인", response.getBody());
    }

    @ApiOperation(value = "문장 추출 생성", notes = "문장 추출을 생성합니다")
    @Cacheable(value = "sentences", cacheManager = "cacheManager")
    @PostMapping("/sentences")
    public CommonResponse findSentences(@RequestBody SentenceRequest sentenceRequest) {

        ResponseEntity<Object> response = infoEngineClient.getSentences(sentenceRequest);

        if(!response.getStatusCode().is2xxSuccessful()) {
            return CommonResponse.withMessage("외부 모듈 에러");
        }

        return CommonResponse.withMessageAndData("문장 추출 생성 요청 확인", response.getBody());
    }
}
