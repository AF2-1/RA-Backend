package kr.co.tmax.rabackend.interfaces.simulation;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.Strategy.PortfolioValue;
import kr.co.tmax.rabackend.domain.strategy.Strategy.PortfolioWeight;
import lombok.*;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimulationDto {

    @Getter
    @Setter
    @ToString
    public static class RegisterSimulationRequest {
        @NotEmpty
        private String userId;
        @NotEmpty @Size(min = 2, max = 10)
        private List<String> assets;
        @Min(1) @Max(365)
        private int rebalancingPeriod;
        private LocalDate startDate;
        private LocalDate endDate;
        @NotEmpty @Size(max = 5)
        private List<String> strategies;
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
        public RegisterStrategyRequest(String strategy, int rebalancingLen, List<String> assetList, LocalDate startDate, LocalDate endDate, String callbackUrl) {
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
        private List<Double> recommendedPf;
        private Map<String, List<Double>> inferenceResults;
        private EvaluationResults evaluationResults;
        private Map<String, List<Double>> dailyPfWeights;
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

            return SimulationCommand.CompleteStrategyRequest.builder()
                    .evaluationResults(result)
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

        public static SimulationsResponse create(String userId, List<Simulation> simulations) {
            List<SimpleSimulationResponse> getSimulationResponses = simulations.stream()
                    .map(SimpleSimulationResponse::create)
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
        private List<String> assets;
        private LocalDate startDate;
        private LocalDate endDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdDatetime;
        private int rebalancingPeriod;
        private boolean isDone;
        private List<SimpleStrategyResponse> strategies;

        public static SimpleSimulationResponse create(Simulation simulation) {
            List<SimpleStrategyResponse> strategies = simulation.getStrategies()
                    .entrySet()
                    .stream()
                    .map(strategyEntry -> SimpleStrategyResponse.create(strategyEntry.getKey(), strategyEntry.getValue().isDone()) )
                    .collect(Collectors.toList());

            List<String> assets = simulation.getAssets().stream()
                    .map(Asset::getName)
                    .collect(Collectors.toList());

            return SimpleSimulationResponse.builder()
                    .simulationId(simulation.getSimulationId())
                    .assets(assets)
                    .isDone(simulation.isDone())
                    .rebalancingPeriod(simulation.getRebalancingPeriod())
                    .startDate(simulation.getStartDate())
                    .endDate(simulation.getEndDate())
                    .createdDatetime(simulation.getCreatedDatetime())
                    .strategies(strategies)
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

        public static SimulationResponse create(Simulation simulation) {
            List<StrategyResponse> strategies = simulation.getStrategies()
                    .entrySet()
                    .stream()
                    .map(strategyEntry -> StrategyResponse.create(strategyEntry.getKey(), strategyEntry.getValue()))
                    .collect(Collectors.toList());

            List<AssetResponse> assets = simulation.getAssets().stream()
                    .map(AssetResponse::create)
                    .collect(Collectors.toList());

            return SimulationResponse.builder()
                    .simulationId(simulation.getSimulationId())
                    .assets(assets)
                    .isDone(simulation.isDone())
                    .rebalancingPeriod(simulation.getRebalancingPeriod())
                    .startDate(simulation.getStartDate())
                    .endDate(simulation.getEndDate())
                    .createdDatetime(simulation.getCreatedDatetime())
                    .strategies(strategies)
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class SimpleStrategyResponse {
        private String name;
        private boolean done;

        public static SimpleStrategyResponse create(String name, boolean isDone) {
            return SimpleStrategyResponse.builder()
                    .name(name)
                    .done(isDone)
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class StrategyResponse {
        private String name;
        private boolean done;
        // todo: 아래 필드 구체화  필요
        private Strategy.EvaluationResults evaluationResults;
        private List<PortfolioWeight> dailyPfWeights;
        private List<PortfolioValue> dailyPfValues;

        public static StrategyResponse create(String strategyName, Strategy strategy) {
            return StrategyResponse.builder()
                    .evaluationResults(strategy.getEvaluationResults())
                    .name(strategyName)
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
}
