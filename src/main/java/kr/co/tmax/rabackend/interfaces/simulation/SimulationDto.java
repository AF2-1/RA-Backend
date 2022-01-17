package kr.co.tmax.rabackend.interfaces.simulation;

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
}
