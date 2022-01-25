package kr.co.tmax.rabackend.domain.strategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Strategy {
    private String name;
    private boolean done = false;
    private LocalDateTime trainedTime = null;
    private List<PortfolioWeight> rebalancingWeights = new ArrayList<>();
    private List<PortfolioWeight> dailyWeights = new ArrayList<>();
    private List<PortfolioValue> dailyValues = new ArrayList<>();

    public Strategy(String name) {
        this.name = name;
    }

    public void complete() {
        this.done = true;
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
    public class PortfolioWeight {
        private LocalDateTime date;
        private List<Double> weights;
    }

    @Getter
    @AllArgsConstructor
    public class PortfolioValue {
        private LocalDateTime date;
        private Double weight;
    }
}
