package kr.co.tmax.rabackend.domain.srategy;

import lombok.Getter;

@Getter
public class Strategy {
    private String name;
    private boolean isDone;

    public Strategy(String name) {
        this.name = name;
        this.isDone = false;
    }
}
