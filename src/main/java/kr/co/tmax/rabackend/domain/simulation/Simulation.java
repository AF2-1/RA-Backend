package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.domain.strategy.Strategy;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@ToString
@Getter
@Document(collection = "simulations")
@NoArgsConstructor
public class Simulation {
    @Id
    private String simulationId;
    private String userId;
    private int cnt;
    private boolean isDone;
    private List<String> assets;
    private int rebalancingPeriod;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdDatetime;
    private List<Strategy> strategies;

    @Builder
    public Simulation(String userId, List<String> assets, int rebalancingPeriod, LocalDate startDate, LocalDate endDate, List<Strategy> strategies,
                      int cnt) {
        this.simulationId = UUID.randomUUID().toString();
        this.isDone = false;
        this.createdDatetime = LocalDateTime.now();
        this.userId = userId;
        this.assets = assets;
        this.rebalancingPeriod = rebalancingPeriod;
        this.startDate = startDate;
        this.endDate = endDate;
        this.strategies = strategies;
        this.cnt = cnt;
    }

    public void update(String strategyName) {
        for (Strategy strategy : strategies) {
            if (!strategy.getName().equals(strategyName))
                continue;
            strategy.complete();
            cnt++;
        }

        if (cnt == strategies.size()) {
            isDone = true;
        }
    }
}
