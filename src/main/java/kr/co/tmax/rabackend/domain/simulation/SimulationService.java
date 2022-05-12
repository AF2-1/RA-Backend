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
import kr.co.tmax.rabackend.domain.strategy.StrategyRank;
import kr.co.tmax.rabackend.infrastructure.user.UserRepository;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
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
                command.getDailyWeights(), command.getDailyValues());
        strategyStore.store(strategy);

        log.debug("strategy is completed with simulationId: {} | strategyName: {} | userId: {}",
                command.getSimulationId(), command.getStrategyName(), simulation.getUserId());
    }

    public SimulationDto.DashBoardResponse getDashBoard() {
        List<StrategyRank> rankingsAboutCagr = strategyReader.findFiveRanksAboutCagr();

        List<SimulationDto.Ranker> rankers = new ArrayList<>(rankingsAboutCagr.size());

        int ranking = 1;
        for (var rankingAboutCagr : rankingsAboutCagr) {
            var ranker = userRepository.findById(rankingAboutCagr.getUserId()).get();

            rankers.add(
                    SimulationDto.Ranker.builder()
                            .ranking(ranking)
                            .cagr(rankingAboutCagr.getEvaluationResults().getCagr())
                            .simulationId(rankingAboutCagr.getSimulation().get(0).get_id())
                            .strategyName(rankingAboutCagr.getStrategyName())
                            .email(ranker.getEmail())
                            .name(ranker.getName())
                            .build()
            );

            ranking++;
        }

        return SimulationDto.DashBoardResponse.create(rankers);

//        Set<String> userset = new HashSet();
//        List<Double> cagrset = new ArrayList<>();
//        List<Strategy> all = strategyReader.findAll(Sort.by(Sort.Direction.DESC, "evaluationResults.cagr"));
//        for (int i = 0; i < all.size(); i++) {
//            Simulation simulation = simulationReader.findById(all.get(i).getSimulationId()).orElseThrow(null);
//            if (all.get(i).getEvaluationResults().getCagr() != null && simulation.getUserId() != null) {
//                userset.add(simulation.getUserId());
//                if (userset.size() > 4) {
//                    break;
//                }
//            }
//        }
//
//        Iterator<String> iter = userset.iterator();
//        List<SimulationDto.Ranker> rankers = new ArrayList<>();
//        SimulationDto.DashBoardResponse dashBoardResponse = new SimulationDto.DashBoardResponse();
//        while (iter.hasNext()) {
//            SimulationDto.Ranker ranker = new SimulationDto.Ranker();
//            User user = userRepository.findById(iter.next()).orElseThrow(null);
//            ranker.setEmail(user.getEmail());
//            ranker.setName(user.getName());
//            rankers.add(ranker);
//            dashBoardResponse.setRankers(rankers);
//        }
//        return dashBoardResponse;
    }
}
