package kr.co.tmax.rabackend.interfaces.simulation;

import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.domain.asset.AssetReader;
import kr.co.tmax.rabackend.domain.strategy.StrategyReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("SimulationRegisterRequestValidator는")
class SimulationRegisterRequestValidatorTest {

    @InjectMocks
    protected SimulationRegisterRequestValidator simulationRegisterRequestValidator;

    @Mock
    protected AppProperties appProperties;

    @Mock
    protected AssetReader assetReader;

    @Mock
    protected StrategyReader strategyReader;

    //TODO: 구현
}