package kr.co.tmax.rabackend.domain.srategy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Strategy {
    private String name;
    private boolean done;
    // todo: 아래 필드 구체화 필요
    private Object inferenceResults;
    private Object evaluationResults;
    private Object dailyPfWeights;
    private Object dailyPfValues;

    public Strategy(String name) {
        this.name = name;
        this.done = false;
    }
}
