package kr.co.tmax.rabackend.domain.simulation;

import antlr.collections.List;
import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetCommand;
import kr.co.tmax.rabackend.domain.asset.AssetReader;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.StrategyReader;
import kr.co.tmax.rabackend.domain.strategy.StrategyStore;
import kr.co.tmax.rabackend.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationServiceTest {

    @InjectMocks
    private SimulationService simulationService;

    @Mock
    private SimulationStore simulationStore;
    @Mock
    private SimulationReader simulationReader;
    @Mock
    private StrategyStore strategyStore;
    @Mock
    private StrategyReader strategyReader;
    @Mock
    private AssetReader assetReader;
    @Mock
    private WebClient webClient;
    @Mock
    private AppProperties appProperties;

    private Simulation simulation;
    private Strategy strategy;
    private SimulationCommand.RegisterSimulationRequest registerSimulationRequest;
    private Asset asset;

    @BeforeEach
    void setUp() {
        simulation = Simulation.builder()
                .rebalancingPeriod(0)
                .userId("2")
                .endDate(LocalDate.MIN)
                .startDate(LocalDate.MIN)
                .assets(new ArrayList<>())
                .build();

        strategy = new Strategy();

        var assetCommands = new ArrayList<AssetCommand>();
        assetCommands.add(new AssetCommand("KOSPI", "033780.KS"));
        assetCommands.add(new AssetCommand("KOSPI", "033780.KS"));
        assetCommands.add(new AssetCommand("KOSPI", "033780.KS"));

        var strategyRequest = new ArrayList<String>();
        strategyRequest.add("ew");
        strategyRequest.add("iv");
        strategyRequest.add("rp");

        registerSimulationRequest = SimulationCommand.RegisterSimulationRequest.builder()
                .userId("2")
                .cnt(1)
                .endDate(LocalDate.MIN)
                .startDate(LocalDate.MIN)
                .rebalancingPeriod(40)
                .assets(assetCommands)
                .strategies(strategyRequest)
                .build();

        asset = Asset.builder()
                .id("1")
                .ticker("033780.KS")
                .index("KOSPI")
                .name("KT & G KT&G")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName(value = "새로운 Simulation을 등록할 수 있다.")
    void registerSimulationTest() {
        // given
        given(assetReader.searchByTickerAndIndex(anyString(), anyString())).willReturn(Optional.of(asset));
        given(simulationStore.store(any(Simulation.class))).willReturn(simulation);
        given(strategyStore.store(any(Strategy.class))).willReturn(strategy);
//        BDDMockito.doNothing().doThrow(RuntimeException.class).when(strategyStore);
        given(appProperties.getAi()).willReturn(any());
        given(appProperties.getAi().getCallBackUrl()).willReturn(anyString());
        given(appProperties.getAi().getPath()).willReturn(anyString());

        // when
        simulationService.registerSimulation(registerSimulationRequest);

        // then
        then(assetReader).should(BDDMockito.atLeast(1)).searchByTickerAndIndex(anyString(), anyString());
        then(simulationStore).should().store(any());
        then(strategyStore).should(BDDMockito.atLeast(1)).store(any(Strategy.class));
    }

    @Test
    @DisplayName(value = "UserId로 simulation을 전체 조회할 수 있다.")
    void getSimulationsTest() {
        // given
        given(simulationReader.findByUserId(anyString())).willReturn(new ArrayList<Simulation>());

        // when
        simulationService.getSimulations(new SimulationCommand.GetSimulationsRequest(anyString()));

        // then
        then(simulationReader).should().findByUserId(anyString());
    }

    @Test
    @DisplayName(value = "simulation 단건조회를 할 수 있다.")
    void getSimulationTest() {
        // given
        given(simulationReader.findByUserIdAndSimulationId(anyString(), anyString())).willReturn(Optional.of(new Simulation()));

        // when
        simulationService.getSimulation(new SimulationCommand.GetSimulationRequest(anyString(), anyString()));

        // then
        then(simulationReader).should().findByUserIdAndSimulationId(anyString(), anyString()).orElseThrow();
    }

    @Test
    @DisplayName(value = "simulation 단건조회 중 찾을 수 없으면 예외를 던진다.")
    void getSimulationFailTest() {
        // given
        given(simulationReader.findByUserIdAndSimulationId(anyString(), anyString())).willThrow(ResourceNotFoundException.class);

        // when
        simulationService.getSimulation(new SimulationCommand.GetSimulationRequest(anyString(), anyString()));

        // then
        then(simulationReader).should().findByUserIdAndSimulationId(anyString(), anyString());
    }

    @Test
    @DisplayName(value = "simulation을 삭제할 수 있다")
    void deleteSimulationTest() {
        // given
        given(simulationReader.findById(anyString())).willReturn(Optional.of(simulation));
        BDDMockito.doNothing().when(simulationStore).delete(simulation);

        // when
        simulationService.deleteSimulation(new SimulationCommand.DeleteSimulationRequest(anyString(), anyString()));

        // then
        then(simulationReader).should().findById(anyString());
        then(simulationStore).should().delete(any(Simulation.class));
    }

    @Test
    @DisplayName(value = "userId가 다르면 BadRequestException가 발생한다.")
    void deleteSimulationFailTest() {
        // given

        // when

        // then
    }
}
