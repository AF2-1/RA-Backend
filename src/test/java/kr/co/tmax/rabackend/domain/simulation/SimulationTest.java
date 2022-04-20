package kr.co.tmax.rabackend.domain.simulation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Simulation에서")
class SimulationTest {

    @Test
    @DisplayName("Simulation의 상태를 완료로 바꿀 수 있다.")
    void completeTest() {
        Simulation simulation = new Simulation();

        simulation.complete();

        assertTrue(simulation.isDone());
    }
}