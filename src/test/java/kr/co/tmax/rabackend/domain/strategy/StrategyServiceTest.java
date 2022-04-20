package kr.co.tmax.rabackend.domain.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class StrategyServiceTest {

    @InjectMocks
    protected StrategyService strategyService;

    @Mock
    protected StrategyReader strategyReader;

    @Test
    @DisplayName("simulation을 기반으로 모든 전략을 찾을 수 있다.")
    void findAllBySimulation() {
        // given
        given(strategyReader.findAllBySimulationId(anyString())).willReturn(any());

        // when
        strategyService.findAllBySimulation(UUID.randomUUID().toString());

        //then
        then(strategyReader).should(BDDMockito.times(1)).findAllBySimulationId(anyString());
    }
}