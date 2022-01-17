package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.domain.srategy.Strategy;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ToString
@Getter
@Document(collection = "simulations")
@NoArgsConstructor
public class Simulation {
    @Id
    private String simulationId;
    private String userId;
    private boolean isDone;
    private List<String> assets;
    private int rebalancingPeriod;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdDatetime;
    private List<Strategy> strategies;

    @Builder(builderClassName = "createBuilder", builderMethodName = "createBuilder")
    public Simulation(String userId, List<String> assets, int rebalancingPeriod, LocalDate startDate, LocalDate endDate, List<Strategy> strategies) {
        this.simulationId = UUID.randomUUID().toString();
        this.isDone = false;
        this.createdDatetime = LocalDateTime.now();
        this.userId = userId;
        this.assets = assets;
        this.rebalancingPeriod = rebalancingPeriod;
        this.startDate = startDate;
        this.endDate = endDate;
        this.strategies = strategies;
    }

    @Builder(builderClassName = "updateBuilder", builderMethodName = "updateBuilder")
    public Simulation(String simulationId, String userId, List<String> assets, int rebalancingPeriod, LocalDate startDate, LocalDate endDate, List<Strategy> strategies) {
        this.simulationId = simulationId;
        this.isDone = false;
        this.createdDatetime = LocalDateTime.now();
        this.userId = userId;
        this.assets = assets;
        this.rebalancingPeriod = rebalancingPeriod;
        this.startDate = startDate;
        this.endDate = endDate;
        this.strategies = strategies;
    }

    @Builder(builderClassName = "doneSimulation", builderMethodName = "doneSimulation")
    public Simulation(String simulationId, boolean isDone, String userId, List<String> assets, int rebalancingPeriod, LocalDate startDate, LocalDate endDate, List<Strategy> strategies) {
        this.simulationId = simulationId;
        this.isDone = isDone;
        this.createdDatetime = LocalDateTime.now();
        this.userId = userId;
        this.assets = assets;
        this.rebalancingPeriod = rebalancingPeriod;
        this.startDate = startDate;
        this.endDate = endDate;
        this.strategies = strategies;
    }
}
