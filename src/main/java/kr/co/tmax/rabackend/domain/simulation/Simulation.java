package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    private List<Asset> assets;
    private int rebalancingPeriod;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdDatetime;
    private ConcurrentHashMap<String, Strategy> strategies = new ConcurrentHashMap<>();

    @Builder
    public Simulation(String userId, List<Asset> assets, int rebalancingPeriod, LocalDate startDate, LocalDate endDate) {
        this.simulationId = UUID.randomUUID().toString();
        this.userId = userId;
        this.isDone = false;
        this.cnt = 0;
        this.createdDatetime = LocalDateTime.now();
        this.assets = assets;
        this.rebalancingPeriod = rebalancingPeriod;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addStrategy(String name, Strategy strategy) {
        strategies.put(name, strategy);
    }

    public void updateCnt() {
        cnt++;
        if (cnt == strategies.size())
            this.isDone = true;
    }
}
