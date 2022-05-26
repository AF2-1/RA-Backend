package kr.co.tmax.rabackend.external;

import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.exception.ExternalException;
import kr.co.tmax.rabackend.interfaces.info.InfoDto;
import kr.co.tmax.rabackend.interfaces.info.InfoDto.KeywordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class InfoEngineClientImpl {

    private final WebClient webClient;
    private final AppProperties appProperties;

    public ResponseEntity<Object> getKeywords(KeywordRequest keywordRequest) {
        String infoUrl = appProperties.getInfo().getUrl() + appProperties.getInfo().getKeywordsPath();

        return webClient.post()
                .uri(infoUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(keywordRequest)
                .retrieve()
                .toEntity(Object.class)
                .onErrorMap(e -> {
                    throw new ExternalException(e.getMessage());
                })
                .block();
    }

    public ResponseEntity<Object> getSentences(InfoDto.SentenceRequest sentenceRequest) {
        String infoUrl = appProperties.getInfo().getUrl() + appProperties.getInfo().getSentencesPath();

        return webClient.post()
                .uri(infoUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sentenceRequest)
                .retrieve()
                .toEntity(Object.class)
                .onErrorMap(e -> {
                    throw new ExternalException(e.getMessage());
                })
                .block();
    }
}
