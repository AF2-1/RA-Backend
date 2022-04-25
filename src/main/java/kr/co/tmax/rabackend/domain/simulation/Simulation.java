package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.domain.asset.Asset;
import lombok.*;
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
@Setter
@Document(collection = "simulations")
@NoArgsConstructor
public class Simulation {
    @Id
    private String simulationId;
    private String userId;
    private boolean isDone;
    private List<Asset> assets;
    private int rebalancingPeriod;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdDatetime;

    @Builder
    public Simulation(String userId, List<Asset>
            assets, int rebalancingPeriod, LocalDate startDate,
                      LocalDate endDate) {
        this.simulationId = UUID.randomUUID().toString();
        this.userId = userId;
        this.isDone = false;
//        this.cnt = 0;
        this.createdDatetime = LocalDateTime.now();
        this.assets = assets;
        this.rebalancingPeriod = rebalancingPeriod;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void complete() {
        isDone = true;
    }

//    public synchronized void updateCnt() {
//        cnt++;
//        if (cnt == numOfStrategies)
//            this.isDone = true;
//    }
}
