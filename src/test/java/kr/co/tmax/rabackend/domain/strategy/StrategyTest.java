package kr.co.tmax.rabackend.domain.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

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

    @Test
    @DisplayName("VO객체를 만들 수 있다.")
    void vOCreationTest() {
        var testDateStr = LocalDate.now().toString();

        var portfolioWeight = new Strategy.PortfolioWeight(testDateStr, Arrays.asList(1.1, 0.1));
        var portfolioValue = new Strategy.PortfolioValue(testDateStr, 1.1);

        assertTrue(portfolioWeight.getDate().toString().equals(testDateStr));
        assertTrue(portfolioValue.getDate().toString().equals(testDateStr));
    }
}