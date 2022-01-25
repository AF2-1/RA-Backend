package kr.co.tmax.rabackend.domain.strategy;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Strategy {
    private boolean done;
    private LocalDate trainedTime = null;
    private List<PortfolioWeight> rebalancingWeights = new ArrayList<>();
    private List<PortfolioWeight> dailyWeights = new ArrayList<>();
    private List<PortfolioValue> dailyValues = new ArrayList<>();

    public Strategy() {
        this.done = false;
    }

    public void complete(LocalDate trainedTime,
                         List<PortfolioWeight> rebalancingWeights,
                         List<PortfolioWeight> dailyWeights,
                         List<PortfolioValue> dailyValues) {
        this.done = true;
        this.trainedTime = trainedTime;
        this.rebalancingWeights = rebalancingWeights;
        this.dailyWeights = dailyWeights;
        this.dailyValues = dailyValues;
    }

    @Getter
    @AllArgsConstructor
    public class EvaluationResults {
        private Double totalReturn;
        private Double volatility;
        private Double cagr;
        private Double sharpeRatio;
        private Double sortinoRatio;
        private Double mdd;
        private Double turnOver;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PortfolioWeight {
        private LocalDate date;
        private List<Double> weights;

        public PortfolioWeight(String date, List<Double> weights) {
            this.date = LocalDate.parse(date);
            this.weights = weights;
        }
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PortfolioValue {
        private LocalDate date;
        private Double weight;

        public PortfolioValue(String date, Double weight) {
            this.date = LocalDate.parse(date);
            this.weight = weight;
        }
    }
}
