package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.Strategy.PortfolioValue;
import kr.co.tmax.rabackend.domain.strategy.Strategy.PortfolioWeight;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SimulationCommand {

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class RegisterSimulationRequest {
        private String userId;
        private int cnt;
        private List<String> assets;
        private int rebalancingPeriod;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<String> strategies;
    }

    @Setter
    @Getter
    @ToString
    @AllArgsConstructor
    public static class GetSimulationsRequest {
        private String userId;
    }

    @Getter
    @AllArgsConstructor
    public static class GetSimulationRequest {
        private String userId;
        private String simulationId;
    }

    @Getter
    @AllArgsConstructor
    public static class DeleteSimulationRequest {
        private String userId;
        private String simulationId;
    }

    @ToString
    @Builder
    @Getter
    @AllArgsConstructor
    public static class CompleteStrategyRequest {
        private String simulationId;
        private String strategyName;
        LocalDate trainedTime;
        Strategy.EvaluationResults evaluationResults;
        List<PortfolioWeight> rebalancingWeights;
        List<PortfolioWeight> dailyWeights;
        List<PortfolioValue> dailyValues;
    }
}
