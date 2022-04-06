package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetReader;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand.*;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.StrategyReader;
import kr.co.tmax.rabackend.domain.strategy.StrategyStore;
import kr.co.tmax.rabackend.exception.BadRequestException;
import kr.co.tmax.rabackend.exception.ResourceNotFoundException;
import kr.co.tmax.rabackend.external.KserveApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SimulationService {

    private final SimulationStore simulationStore;
    private final SimulationReader simulationReader;
    private final StrategyStore strategyStore;
    private final StrategyReader strategyReader;
    private final AssetReader assetReader;
    private final KserveApiClient kserveApiClient;

    public void registerSimulation(RegisterSimulationRequest request) {
        List<Asset> assets = request.getAssets().stream().map(a ->
                assetReader.searchByTickerAndIndex(a.getTicker(), a.getIndex()).orElse(new Asset())).collect(Collectors.toList());

        log.debug("registerSimulation called | assets: {}", assets);
        Simulation simulation = Simulation.builder()
                .rebalancingPeriod(request.getRebalancingPeriod())
                .userId(request.getUserId())
                .endDate(request.getEndDate())
                .startDate(request.getStartDate())
                .assets(assets)
                .build();

        final Simulation storedSimulation = simulationStore.store(simulation);

        request.getStrategies().forEach(strategyName -> strategyStore.store(new Strategy(strategyName, storedSimulation.getSimulationId())));

        kserveApiClient.requestAA(simulation, request.getStrategies());
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

    public void completeStrategy(CompleteStrategyRequest command) {
//        Simulation simulation = simulationReader.findById(command.getSimulationId()).orElseThrow(
//                () -> new ResourceNotFoundException("Simulation", "simulationId", command.getSimulationId()));

        Strategy strategy = strategyReader.findBySimulationIdAndName(command.getSimulationId(), command.getStrategyName()).orElseThrow(
                () -> new ResourceNotFoundException(command.getStrategyName(), command.getSimulationId(), command.getStrategyName()));

        strategy.complete(command.getTrainedTime(), command.getEvaluationResults(), command.getRecommendedPf(),
                command.getRebalancingWeights(),
                command.getDailyWeights(), command.getDailyValues());

//        simulation.updateCnt();
//        simulationStore.store(simulation);
        strategyStore.store(strategy);

        log.debug("strategy is completed with simulationId: {} | strategyName: {}", command.getSimulationId(), command.getStrategyName());
    }
}
