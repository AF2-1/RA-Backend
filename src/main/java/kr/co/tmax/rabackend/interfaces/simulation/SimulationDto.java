package kr.co.tmax.rabackend.interfaces.simulation;

import kr.co.tmax.rabackend.domain.simulation.Simulation;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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
    @Builder
    @AllArgsConstructor
    public static class RegisterStrategyRequest {
        private String strategy;
        private List<String> assetList;
        private int rebalancingLen;
        private LocalDate startDate;
        private LocalDate endDate;
        private String callbackUrl;
        private boolean indexRequest = false;
    }

    @Getter
    @Builder
    @ToString
    public static class RegisterResponse {

        private final String itemToken;
    }
}
