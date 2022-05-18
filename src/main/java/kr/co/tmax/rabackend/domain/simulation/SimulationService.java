package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetReader;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand.*;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.StrategyReader;
import kr.co.tmax.rabackend.domain.strategy.StrategyStore;
import kr.co.tmax.rabackend.domain.user.User;
import kr.co.tmax.rabackend.exception.BadRequestException;
import kr.co.tmax.rabackend.exception.ResourceNotFoundException;
import kr.co.tmax.rabackend.external.KserveApiClient;
import kr.co.tmax.rabackend.domain.strategy.StrategyRank;
import kr.co.tmax.rabackend.infrastructure.user.UserRepository;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto.DashBoardDetailResponse;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto.Ranker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private final UserRepository userRepository;

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

        request.getStrategies().forEach(strategyName -> strategyStore.store(
                new Strategy(strategyName, storedSimulation.getSimulationId(), storedSimulation.getUserId())));

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
        List<Strategy> allBySimulationId = strategyReader.findAllBySimulationId(command.getSimulationId());
        for (int i = 0; i < allBySimulationId.size(); i++) {
            strategyStore.delete(allBySimulationId.get(i));
        }
        simulationStore.delete(simulation);
    }

    public void completeStrategy(CompleteStrategyRequest command) {
        Strategy strategy = strategyReader.findBySimulationIdAndName(command.getSimulationId(), command.getStrategyName()).orElseThrow(
                () -> new ResourceNotFoundException(command.getStrategyName(), command.getSimulationId(), command.getStrategyName()));

        Simulation simulation = simulationReader.findById(command.getSimulationId()).orElseThrow(null);

        strategy.complete(command.getTrainedTime(), simulation.getUserId(), command.getEvaluationResults(), command.getRecommendedPf(),
                command.getRebalancingWeights(),
                command.getDailyWeights(), command.getDailyValues(), simulation.getAssets());
        strategyStore.store(strategy);

        log.debug("strategy is completed with simulationId: {} | strategyName: {} | userId: {}",
                command.getSimulationId(), command.getStrategyName(), simulation.getUserId());
    }

    public List<Ranker> getDashBoard() {
        List<Ranker> rankersByCagr = strategyReader.findRankersByCagr();
        List<Ranker> rankers = new ArrayList<>(rankersByCagr.size());

        int ranking = 1;
        for (Ranker ranker : rankersByCagr) {
            rankers.add(Ranker.builder()
                    .ranking(ranking)
                    .userId(ranker.getUserId())
                    .cagr(ranker.getCagr())
                    .strategyName(ranker.getStrategyName())
                    .simulationId(ranker.getSimulationId())
                    .assets(ranker.getAssets())
                    .build());
            ranking++;
        }
        return rankers;
    }

    public DashBoardDetailResponse getDashBoardDetail(String simulationId, String strategyName) {
        Simulation simulation = simulationReader.findById(simulationId).orElseThrow(null);
        Strategy strategy = strategyReader.findBySimulationIdAndName(simulationId, strategyName).orElseThrow(null);
        return DashBoardDetailResponse.builder()
                .simulationId(simulationId)
                .assets(simulation.getAssets())
                .startDate(simulation.getStartDate())
                .endDate(simulation.getEndDate())
                .createdDate(simulation.getCreatedDatetime())
                .rebalancingPeriod(simulation.getRebalancingPeriod())
                .isDone(simulation.isDone())
                .strategy(strategy)
                .build();
    }
}
