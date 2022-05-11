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
        String callbackUrl = MessageFormat.format(appProperties.getTrading().getCallBackUrl(), portfolio.getId().toString());
        String requestUrl = appProperties.getTrading().getEngineAddress() + MessageFormat.format(appProperties.getTrading().getPath(), portfolio.getId().toString());

        log.info("ready for send request to Trading engine");

        var response = webClient.post()
                .uri(requestUrl)
                .header("callbackUrl", callbackUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(portfolio)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorMap(e -> {
                    throw new ExternalException(e.getMessage());
                })
                .block();

        log.info("Success for send request to Trading engine {}", response);
    }
}
