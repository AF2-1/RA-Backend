package kr.co.tmax.rabackend.domain.strategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Getter
    @AllArgsConstructor
    public static class PortfolioWeight {
        private LocalDateTime date;
        private List<Double> weights;

        public PortfolioWeight(String date, List<Double> weights) {
            this.date = LocalDateTime.parse(date);
            this.weights = weights;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class PortfolioValue {
        private LocalDateTime date;
        private Double weight;

        public PortfolioValue(String date, Double weight) {
            this.date = LocalDateTime.parse(date);
            this.weight = weight;
        }
    }
}
