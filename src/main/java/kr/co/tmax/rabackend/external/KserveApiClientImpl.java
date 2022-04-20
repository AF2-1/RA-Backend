package kr.co.tmax.rabackend.external;

import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KserveApiClientImpl implements KserveApiClient {
    private final WebClient webClient;
    private final AppProperties appProperties;

    /**
     * request to Inference Server to get the weights from AA(Asset Allocation)
     * Algo(Strategy)
     *
     * @param simulation
     */
    @Override
    public void requestAA(Simulation simulation, List<String> strategyNames) {
        // todo: AI 서버에서 실패 응답이 온 경우 예외를 던져 Simulation이 DB에 저장되는 것을 막아야한다.
        strategyNames
                .forEach(strategyName -> {
                    SimulationDto.RegisterStrategyRequest requestBody = createRequest(simulation, strategyName);
                    SimulationDto.RegisterStrategyResponse response = executeRequest(requestBody);
                    log.debug("AI API Called | simulationId: {} strategyName: {} AI response:{}",
                            simulation.getSimulationId(), strategyName, response.toString());
                });
    }

    private SimulationDto.RegisterStrategyRequest createRequest(Simulation simulation, String strategyName) {
        String callbackUrl = String.format("%s?simulationId=%s&strategyName=%s",
                appProperties.getAi().getCallBackUrl(),
                simulation.getSimulationId(),
                strategyName);

        SimulationDto.RegisterStrategyRequest request = SimulationDto.RegisterStrategyRequest.builder()
                .strategy(strategyName)
                .rebalancingLen(simulation.getRebalancingPeriod())
                .assetList(simulation.getAssets().stream().map(Asset::getTicker).collect(Collectors.toList()))
                .startDate(simulation.getStartDate())
                .endDate(simulation.getEndDate())
                .callbackUrl(callbackUrl)
                .build();

        log.debug("AI API request body: {}", request);

        return request;
    }

    private SimulationDto.RegisterStrategyResponse executeRequest(SimulationDto.RegisterStrategyRequest requestBody) {
        return webClient.post()
                .uri(appProperties.getAi().getPath())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(SimulationDto.RegisterStrategyResponse.class)
                .block();
    }
}
