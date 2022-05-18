package kr.co.tmax.rabackend.external;

import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.domain.trading.Portfolio;
import kr.co.tmax.rabackend.exception.ExternalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.MessageFormat;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradingEngineClientImpl implements TradingEngineClient{
    private final WebClient webClient;
    private final AppProperties appProperties;

    @Override
    public void requestPortfolioCreation(Portfolio portfolio) {
        final var callbackUrl = MessageFormat.format(appProperties.getTrading().getCallBackUrl(), portfolio.getId().toString());
        final var requestUrl = appProperties.getTrading().getEngineAddress() + MessageFormat.format(appProperties.getTrading().getPath(), portfolio.getId().toString());

        final var responseEntity = webClient.post()
                .uri(requestUrl)
                .header("callbackUrl", callbackUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(portfolio)
                .retrieve()
                .toEntity(Void.class)
                .onErrorMap(e -> {
                    log.error("Exception occur while Requesting to Engine with portfolioId = {}", portfolio.getId().toString());
                    throw new ExternalException(e.getMessage());
                })
                .block();

        if(!responseEntity.getStatusCode().is2xxSuccessful()) {
            log.warn("The networking with TS engine is not complete");
            return;
        }

        log.info("Success for send request to TS engine with portfolioId = {}", portfolio.getId().toString());
    }
}
