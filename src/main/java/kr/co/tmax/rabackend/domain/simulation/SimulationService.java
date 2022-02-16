package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetReader;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand.*;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.exception.BadRequestException;
import kr.co.tmax.rabackend.exception.ResourceNotFoundException;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto.RegisterStrategyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
// @Transactional(readOnly = true)
public class SimulationService {

    private final SimulationStore simulationStore;
    private final SimulationReader simulationReader;
    private final AssetReader assetReader;
    private final WebClient webClient;
    private final AppProperties appProperties;

    public void registerSimulation(RegisterSimulationRequest request) {
        List<Asset> assets = assetReader.findByTickerIn(request.getAssets());
        log.debug("registerSimulation called | assets: {}", assets);
        Simulation simulation = Simulation.builder()
                .rebalancingPeriod(request.getRebalancingPeriod())
                .userId(request.getUserId())
                .endDate(request.getEndDate())
                .startDate(request.getStartDate())
                .assets(assets)
                .build();

        Map<String, Strategy> strategies = simulation.getStrategies();
        request.getStrategies().forEach(strategyName -> strategies.put(strategyName, new Strategy()));
        simulationStore.store(simulation);
        requestAA(simulation);
        // return simulationStore.store(simulation);
    }

    /**
     * request to Inference Server to get the weights from AA(Asset Allocation)
     * Algo(Strategy)
     *
     * @param simulation
     */
    private void requestAA(Simulation simulation) {
        // todo: AI 서버에서 실패 응답이 온 경우 예외를 던져 Simulation이 DB에 저장되는 것을 막아야한다.
        simulation.getStrategies()
                .keySet()
                .forEach(strategyName -> {
                    RegisterStrategyRequest requestBody = createRequest(simulation, strategyName);
                    SimulationDto.RegisterStrategyResponse response = executeRequest(requestBody);
                    log.debug("AI API Called | simulationId: {} strategyName: {} AI response:{}",
                            simulation.getSimulationId(), strategyName, response.toString());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {

                    }

                });
    }

    private RegisterStrategyRequest createRequest(Simulation simulation, String strategyName) {
        String callbackUrl = String.format("%s?simulationId=%s&strategyName=%s",
                appProperties.getAi().getCallBackUrl(),
                simulation.getSimulationId(),
                strategyName);

        RegisterStrategyRequest request = RegisterStrategyRequest.builder()
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

    private SimulationDto.RegisterStrategyResponse executeRequest(RegisterStrategyRequest requestBody) {
        return webClient.post()
                .uri(appProperties.getAi().getPath())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(SimulationDto.RegisterStrategyResponse.class)
                .block();
    }

    public List<Simulation> getSimulations(GetSimulationsRequest command) {
        return simulationReader.findByUserId(command.getUserId());
    }

    public Simulation getSimulation(GetSimulationRequest command) {
        return simulationReader.findByUserIdAndSimulationId(command.getUserId(), command.getSimulationId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("simulation", "simulationId", command.getSimulationId()));
    }

    public void deleteSimulation(DeleteSimulationRequest command) {
        Simulation simulation = simulationReader.findById(command.getSimulationId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("simulation", "simulationId", command.getSimulationId()));

        if (!simulation.getUserId().equals(command.getUserId()))
            throw new BadRequestException("simulation의 소유자만 삭제가 가능합니다");

        simulationStore.delete(simulation);
    }

    // @Transactional
    public void completeStrategy(CompleteStrategyRequest command) {
        log.info("전략 이름: {} simulationReader.findById()", command.getStrategyName());
        Simulation simulation = simulationReader.findById(command.getSimulationId()).orElseThrow(
                () -> new ResourceNotFoundException("Simulation", "simulationId", command.getSimulationId()));

        log.info("전략 이름: {} simulation.getStrategies()", command.getStrategyName());
        Map<String, Strategy> strategies = simulation.getStrategies();
        if (!strategies.containsKey(command.getStrategyName()))
            throw new ResourceNotFoundException("Simulation", "Simulation.Strategies", command.getStrategyName());

        log.info("전략 이름: {} strategies.get()", command.getStrategyName());
        Strategy strategy = strategies.get(command.getStrategyName());

        if (strategy.isDone())
            return;

        log.info("전략 이름: {} strategy.complete()", command.getStrategyName());
        strategy.complete(command.getTrainedTime(), command.getEvaluationResults(), command.getRecommendedPf(),
                command.getRebalancingWeights(),
                command.getDailyWeights(), command.getDailyValues());

        log.info("전략 이름: {} simulation.updateCnt()", command.getStrategyName());
        simulation.updateCnt();
        log.info("전략 이름: {} 완료 여부: {}", command.getStrategyName(), strategy.isDone());

        log.info("전략 이름: {} simulationStore.store()", command.getStrategyName());
        simulationStore.store(simulation);
    }
}
