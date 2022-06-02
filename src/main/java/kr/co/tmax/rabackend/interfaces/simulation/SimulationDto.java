package kr.co.tmax.rabackend.interfaces.simulation;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetCommand;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.Strategy.PortfolioValue;
import kr.co.tmax.rabackend.domain.strategy.Strategy.PortfolioWeight;
import kr.co.tmax.rabackend.domain.user.User;
import kr.co.tmax.rabackend.infrastructure.asset.AssetRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SimulationDto {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class RegisterSimulationRequest {
        @NotEmpty
        private String userId;
        @NotEmpty
        @Size(min = 2, max = 10)
        private List<AssetCommand> assets;
        @Min(1)
        @Max(365)
        private int rebalancingPeriod;
        private LocalDate startDate;
        private LocalDate endDate;
        @NotEmpty
        private List<String> strategies;

        @Builder
        public RegisterSimulationRequest(String userId, List<AssetCommand> assets, int rebalancingPeriod, LocalDate startDate, LocalDate endDate, List<String> strategies) {
            this.userId = userId;
            this.assets = assets;
            this.rebalancingPeriod = rebalancingPeriod;
            this.startDate = startDate;
            this.endDate = endDate;
            this.strategies = strategies;
        }
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class RegisterStrategyRequest {
        private String strategy;
        private int rebalancingLen;
        private List<String> assetList;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;
        private String callbackUrl;
        private boolean indexRequest;

        @Builder
        public RegisterStrategyRequest(String strategy, int rebalancingLen, List<String> assetList, LocalDate startDate,
                                       LocalDate endDate, String callbackUrl) {
            this.strategy = strategy;
            this.rebalancingLen = rebalancingLen;
            this.assetList = assetList;
            this.startDate = startDate;
            this.endDate = endDate;
            this.callbackUrl = callbackUrl;
            this.indexRequest = false;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CompleteStrategyRequest {
        private Model model;
        @NotNull
        private List<Double> recommendedPf;
        @NotNull
        private Map<String, List<Double>> inferenceResults;
        @NotNull
        private EvaluationResults evaluationResults;
        @NotNull
        private Map<String, List<Double>> dailyPfWeights;
        @NotNull
        private Map<String, Double> dailyPfValues;

        public SimulationCommand.CompleteStrategyRequest toCommand(String simulationId, String strategyName) {
            ModelMapper modelMapper = new ModelMapper();
            Strategy.EvaluationResults result = modelMapper.map(evaluationResults, Strategy.EvaluationResults.class);

            List<PortfolioWeight> rebalancingWeights = inferenceResults.entrySet()
                    .stream()
                    .map(inferenceResult -> new PortfolioWeight(inferenceResult.getKey(), inferenceResult.getValue()))
                    .collect(Collectors.toList());

            List<PortfolioWeight> dailyWeights = dailyPfWeights
                    .entrySet()
                    .stream()
                    .map(dailyPfWeight -> new PortfolioWeight(dailyPfWeight.getKey(), dailyPfWeight.getValue()))
                    .collect(Collectors.toList());

            List<PortfolioValue> dailyValues = dailyPfValues
                    .entrySet()
                    .stream()
                    .map(dailyPfValue -> new PortfolioValue(dailyPfValue.getKey(), dailyPfValue.getValue()))
                    .collect(Collectors.toList());

            log.info("toCommand 호출");

            return SimulationCommand.CompleteStrategyRequest.builder()
                    .evaluationResults(result)
                    .recommendedPf(recommendedPf)
                    .simulationId(simulationId)
                    .strategyName(strategyName)
                    .dailyValues(dailyValues)
                    .dailyWeights(dailyWeights)
                    .rebalancingWeights(rebalancingWeights)
                    .trainedTime(model.getTrainedTime())
                    .build();
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public class Model {
            private List<String> assets;
            private LocalDate trainedTime;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class EvaluationResults {
            private Double totalReturn;
            private Double volatility;
            private Double cagr;
            private Double sharpeRatio;
            private Double sortinoRatio;
            private Double mdd;
            private Double turnover;
        }
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class RegisterStrategyResponse {
        private String response;
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class SimulationsResponse {
        private String userId;
        private List<SimpleSimulationResponse> simulations;

        public static SimulationsResponse create(String userId, List<Simulation> simulations, List<Strategy> strategies) {
            List<SimpleSimulationResponse> getSimulationResponses = simulations.stream()
                    .map(simulation -> SimpleSimulationResponse.create(simulation, strategies))
                    .collect(Collectors.toList());

            return SimulationsResponse.builder()
                    .userId(userId)
                    .simulations(getSimulationResponses)
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class SimpleSimulationResponse {
        private String simulationId;
        private List<AssetResponse> assets;
        private LocalDate startDate;
        private LocalDate endDate;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdDatetime;
        private int rebalancingPeriod;
        private boolean isDone;
        private List<SimpleStrategyResponse> strategies;

        public static SimpleSimulationResponse create(Simulation simulation, List<Strategy> strategies) {
            List<SimpleStrategyResponse> simpleStrategyResponses = strategies
                    .stream()
                    .map(strategy -> SimpleStrategyResponse.create(strategy))
                    .collect(Collectors.toList());

            List<AssetResponse> assets = simulation.getAssets().stream()
                    .map(AssetResponse::create)
                    .collect(Collectors.toList());

            return SimpleSimulationResponse.builder()
                    .simulationId(simulation.getSimulationId())
                    .assets(assets)
                    .isDone(simulation.isDone() ? true : updateCompletionStatus(simulation, strategies))
                    .rebalancingPeriod(simulation.getRebalancingPeriod())
                    .startDate(simulation.getStartDate())
                    .endDate(simulation.getEndDate())
                    .createdDatetime(simulation.getCreatedDatetime())
                    .strategies(simpleStrategyResponses)
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class SimulationResponse {
        private String simulationId;
        private List<AssetResponse> assets;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDateTime createdDatetime;
        private int rebalancingPeriod;
        private boolean isDone;
        private List<StrategyResponse> strategies;

        public static SimulationResponse create(Simulation simulation, List<Strategy> strategies) {
            boolean done = false;
            List<StrategyResponse> strategyResponse = strategies
                    .stream()
                    .map(strategy -> StrategyResponse.create(strategy))
                    .collect(Collectors.toList());

            List<AssetResponse> assets = simulation.getAssets().stream()
                    .map(AssetResponse::create)
                    .collect(Collectors.toList());

            return SimulationResponse.builder()
                    .simulationId(simulation.getSimulationId())
                    .assets(assets)
                    .isDone(simulation.isDone() ? true : updateCompletionStatus(simulation, strategies))
                    .rebalancingPeriod(simulation.getRebalancingPeriod())
                    .startDate(simulation.getStartDate())
                    .endDate(simulation.getEndDate())
                    .createdDatetime(simulation.getCreatedDatetime())
                    .strategies(strategyResponse)
                    .build();
        }
    }

    private static boolean updateCompletionStatus(Simulation simulation, List<Strategy> strategies) {
        int completedStrategyCnt = (int)strategies.stream().filter(strategy -> strategy.isDone() == true).count();

        System.out.println("completedStrategyCnt = " + completedStrategyCnt);
        System.out.println("strategies = " + strategies.size());
        if(completedStrategyCnt == strategies.size()) {
            simulation.complete();

            return true;
        }

        return false;
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class SimpleStrategyResponse {
        private String name;
        private boolean done;

        public static SimpleStrategyResponse create(Strategy strategy) {
            return SimpleStrategyResponse.builder()
                    .name(strategy.getName())
                    .done(strategy.isDone())
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyResponse {
        private String name;
        private boolean done;
        private List<Double> recommendedPf;
        private Strategy.EvaluationResults evaluationResults;
        private List<PortfolioWeight> dailyPfWeights;
        private List<PortfolioValue> dailyPfValues;

        public static StrategyResponse create(Strategy strategy) {
            return StrategyResponse.builder()
                    .evaluationResults(strategy.getEvaluationResults())
                    .name(strategy.getName())
                    .recommendedPf(strategy.getRecommendedPf())
                    .dailyPfValues(strategy.getDailyValues())
                    .done(strategy.isDone())
                    .dailyPfWeights(strategy.getDailyWeights())
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class AssetResponse {
        private String name;
        private String ticker;

        public static AssetResponse create(Asset asset) {
            return AssetResponse.builder()
                    .name(asset.getName())
                    .ticker(asset.getTicker())
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class SimpleAssetResponse {
        private String name;
        private String index;
        private String ticker;

        public static SimpleAssetResponse create(Asset asset, AssetRepository assetRepository) {
            return SimpleAssetResponse.builder()
                    .name(assetRepository.findByTickerAndIndex(asset.getTicker(), asset.getIndex()).orElseThrow(null).getName())
                    .index(asset.getIndex())
                    .ticker(asset.getTicker())
                    .build();
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor
    public static class Ranker {
        private int ranking;
        private String simulationId;
        private String strategyName;
        private Double cagr;
        private String userId;
        private List<SimpleAssetResponse> assets;

        @Builder
        public Ranker(int ranking, String simulationId, String strategyName, Double cagr, String userId, List<SimpleAssetResponse> assets) {
            this.ranking = ranking;
            this.simulationId = simulationId;
            this.strategyName = strategyName;
            this.cagr = cagr;
            this.userId = userId;
            this.assets = assets;
        }
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class DashBoardDetailResponse {
        private String simulationId;
        private List<Asset> assets;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDateTime createdDate;
        private int rebalancingPeriod;
        private boolean isDone;
        private StrategyResponse strategy;

        @Builder
        public DashBoardDetailResponse(String simulationId, List<Asset> assets, LocalDate startDate, LocalDate endDate, LocalDateTime createdDate,
                                       int rebalancingPeriod, boolean isDone, StrategyResponse strategy) {
            this.simulationId = simulationId;
            this.assets = assets;
            this.startDate = startDate;
            this.endDate = endDate;
            this.createdDate = createdDate;
            this.rebalancingPeriod = rebalancingPeriod;
            this.isDone = isDone;
            this.strategy = strategy;
        }
    }
}
