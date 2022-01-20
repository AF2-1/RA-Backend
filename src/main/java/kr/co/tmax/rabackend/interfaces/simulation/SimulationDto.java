package kr.co.tmax.rabackend.interfaces.simulation;

import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationDto {

    @Getter
    @Setter
    @ToString
    public static class RegisterSimulationRequest {
        private String userId;
        private List<String> assets;
        private int rebalancingPeriod;
        private LocalDate startDate;
        private LocalDate endDate;
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
    public static class GetSimulationsResponse {
        private String userId;
        private List<GetSimulationResponse> simulations;

        public static GetSimulationsResponse create(String userId, List<Simulation> simulations) {
            List<SimulationDto.GetSimulationResponse> getSimulationResponses = simulations.stream()
                    .map(SimulationDto.GetSimulationResponse::create)
                    .collect(Collectors.toList());

            SimulationDto.GetSimulationsResponse getSimulationsResponse = SimulationDto.GetSimulationsResponse.builder()
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
    public static class GetSimulationResponse {
        private String simulationId;
        private List<String> assets;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDateTime createdDatetime;
        private int rebalancingPeriod;
        private boolean isDone;
        private List<GetStrategyResponse> strategies;

        public static GetSimulationResponse create(Simulation simulation) {
            List<SimulationDto.GetStrategyResponse> strategies = simulation.getStrategies().stream()
                    .map(SimulationDto.GetStrategyResponse::create)
                    .collect(Collectors.toList());

            return GetSimulationResponse.builder()
                    .simulationId(simulation.getSimulationId())
                    .assets(simulation.getAssets())
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
    public static class GetStrategyResponse {
        private String name;
        private boolean done;
        // todo: 아래 필드 구체화  필요
        private Object inferenceResults;
        private Object evaluationResults;
        private Object dailyPfWeights;
        private Object dailyPfValues;

        public static GetStrategyResponse create(Strategy strategy) {
            return GetStrategyResponse.builder()
                    .name(strategy.getName())
                    .done(strategy.isDone())
                    .build();
        }
    }
}
