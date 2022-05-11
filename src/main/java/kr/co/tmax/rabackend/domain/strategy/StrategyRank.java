package kr.co.tmax.rabackend.domain.strategy;

import kr.co.tmax.rabackend.domain.asset.Asset;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StrategyRank {
    private String strategyId;

    private String userId;

    private List<RelatedSimulation> simulation;

    private List<Double> recommendedPf;

    private Strategy.EvaluationResults evaluationResults;

    private List<Strategy.PortfolioValue> dailyValues;

    private List<Strategy.PortfolioWeight> dailyWeights;

    private String strategyName;

    private List<Strategy.PortfolioWeight> rebalancingWeights;

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class RelatedSimulation {
        private String _id;

        private String userId;

        private boolean isDone;

        private List<Asset> assets;

        private int rebalancingPeriod;

        private LocalDate startDate;

        private LocalDate endDate;

        private LocalDateTime createdDatetime;
    }

}
