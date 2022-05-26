package kr.co.tmax.rabackend.interfaces.info;

import io.swagger.annotations.ApiOperation;
import kr.co.tmax.rabackend.config.common.CommonResponse;
import kr.co.tmax.rabackend.domain.trading.Portfolio;
import kr.co.tmax.rabackend.exception.ExternalException;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotBlank;

@RequestMapping(value = "/api/v1/")
@RestController
public class InfoController {

    @ApiOperation(value = "키워드 추출 생성", notes = "키워드 추출을 생성합니다")
    @PostMapping("/keywords")
    public ResponseEntity<CommonResponse> registerPortfolio(@RequestBody InfoDto.KeywordRequest keywordRequest) {

        System.out.println("keywordRequest = " + keywordRequest);

        WebClient webClient1 = WebClient.create("http://192.168.153.102:8000");

        Object response = webClient1.post()
                .uri("/en_keyword")
                .bodyValue(keywordRequest)
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorMap(e -> {
                    throw new ExternalException(e.getMessage());
                })
                .block();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> response1 = restTemplate.postForEntity("http://192.168.153.102:8000/en_keyword", keywordRequest, String.class);
        System.out.println("response1 = " + response1);
        System.out.println("response1 = " + response1.getBody());

        System.out.println("response = " + response);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.withMessageAndData("키워드 추출 생성 요청 확인", keywordRequest));
    }

}
