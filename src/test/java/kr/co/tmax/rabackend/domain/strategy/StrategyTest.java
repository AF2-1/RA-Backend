package kr.co.tmax.rabackend.domain.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Strategy에서")
class StrategyTest {

    @Test
    @DisplayName("전략 상태를 완료로 바꿀 수 있다.")
    void completeTest() {
        Strategy strategy = new Strategy(null, null);

        assertFalse(strategy.isDone());

        strategy.complete(null, null, null, null, null, null);

        assertTrue(strategy.isDone());
    }
}