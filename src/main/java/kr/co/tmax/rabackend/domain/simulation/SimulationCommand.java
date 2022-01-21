package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

        public Simulation createWith(List<Asset> assets) {
            return Simulation.builder()
                    .userId(userId)
                    .assets(assets)
                    .strategies(strategies.stream().map(Strategy::new).collect(Collectors.toList()))
                    .startDate(startDate)
                    .endDate(endDate)
                    .rebalancingPeriod(rebalancingPeriod)
                    .cnt(0)
                    .build();
        }
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

    @Getter
    @AllArgsConstructor
    public static class UpdateSimulationRequest {
        private String simulationId;
        private String strategyName;
    }
}
