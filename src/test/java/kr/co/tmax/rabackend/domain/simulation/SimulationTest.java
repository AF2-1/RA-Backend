package kr.co.tmax.rabackend.domain.simulation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Simulation 테스트")
class SimulationTest {

    @Test
    @DisplayName("Simulation을 완료상태로 바꿀 수 있다.")
    void completeTest() {
        Simulation simulation = new Simulation();

        simulation.complete();

        assertTrue(simulation.isDone());
    }
}