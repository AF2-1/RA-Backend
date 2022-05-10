package kr.co.tmax.rabackend.external;

import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.domain.trading.Portfolio;
import kr.co.tmax.rabackend.interfaces.trading.TradingDto;
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
    public void requestPortfolioCreation(Portfolio portfolio, String userId) {
        String callbackUrl = MessageFormat.format(appProperties.getTrading().getCallBackUrl(), portfolio.getPortfolioId());

        webClient.post()
                .uri(MessageFormat.format(appProperties.getTrading().getPath(), userId))
                .header("callbackUrl", callbackUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(portfolio)
                .retrieve()
                .bodyToMono(TradingDto.RegisterPortfolioResponse.class)
                .block();
    }
}