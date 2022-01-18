package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.domain.strategy.Strategy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
        private List<String> assets;
        private int rebalancingPeriod;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<String> strategies;

        public Simulation toEntity() {
            return Simulation.createBuilder()
                    .userId(userId)
                    .assets(assets)
                    .strategies(strategies.stream().map(Strategy::new).collect(Collectors.toList()))
                    .startDate(startDate)
                    .endDate(endDate)
                    .rebalancingPeriod(rebalancingPeriod)
                    .build();
        }
    }
}
