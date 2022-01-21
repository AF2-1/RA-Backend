package kr.co.tmax.rabackend.interfaces.simulation;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        private LocalDate startDate;
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

            SimulationsResponse getSimulationsResponse = SimulationsResponse.builder()
                    .userId(userId)
                    .simulations(getSimulationResponses)
                    .build();

            return getSimulationsResponse;
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
            List<SimpleStrategyResponse> strategies = simulation.getStrategies().stream()
                    .map(SimpleStrategyResponse::create)
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
            List<StrategyResponse> strategies = simulation.getStrategies().stream()
                    .map(StrategyResponse::create)
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
    public static class StrategyResponse {
        private String name;
        private boolean done;
        // todo: 아래 필드 구체화  필요
        private Object inferenceResults;
        private Object evaluationResults;
        private Object dailyPfWeights;
        private Object dailyPfValues;

        public static StrategyResponse create(Strategy strategy) {
            return StrategyResponse.builder()
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
