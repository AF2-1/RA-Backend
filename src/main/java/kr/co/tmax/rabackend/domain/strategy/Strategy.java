package kr.co.tmax.rabackend.domain.strategy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Strategy {
    private String name;
    private boolean done;

    public Strategy(String name) {
        this.name = name;
        this.done = false;
    }
}
