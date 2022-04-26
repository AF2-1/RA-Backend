package kr.co.tmax.rabackend.interfaces.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.tmax.rabackend.domain.asset.AssetCommand;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand;
import kr.co.tmax.rabackend.domain.simulation.SimulationService;
import kr.co.tmax.rabackend.domain.simulation.SimulationStore;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.StrategyService;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("시뮬레이션 컨트롤러 테스트는")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SimulationControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected SimulationController simulationController;

    @MockBean
    protected SimulationService simulationService;

    @MockBean
    protected StrategyService strategyService;

    @MockBean
    protected SimulationStore simulationStore;

    @MockBean
    protected ModelMapper modelMapper;

    @MockBean
    protected SimulationRegisterRequestValidator simulationRegisterRequestValidator;

    @MockBean
    protected BindingResult bindingResult; // 스프링MVC에서 자동으로 생성해주는 영역이기 떄문에 모킹불가(사용할 경우에만 가져다 쓰기)

    @Autowired
    protected MockMvc mockMvc;

    private static String userId;

    private static List<Simulation> simulations;

    @BeforeAll
    static void setUp() {
        userId = UUID.randomUUID().toString();

        simulations = Arrays.asList(
                new Simulation(userId, new ArrayList<>(), 0, null, null),
                new Simulation(userId, new ArrayList<>(), 0, null, null),
                new Simulation(userId, new ArrayList<>(), 0, null, null)
        );
    }

    @Test
    @DisplayName(value = "userId로 simulation들을 조회할 수 있으면 201 Created를 응답한다.")
    void registerSimulationSuccessTest() throws Exception {
        // given
        var requestBody = SimulationDto.RegisterSimulationRequest.builder()
                .userId(userId)
                .assets(Arrays.asList(new AssetCommand("index", "ticker"), new AssetCommand("index", "ticker")))
                .rebalancingPeriod(40)
                .strategies(Arrays.asList(""))
                .build();

        var registerCommand = SimulationCommand.RegisterSimulationRequest.builder().build();

        given(simulationService.getSimulations(any())).willReturn(simulations);  //stub
        BDDMockito.doNothing().when(simulationRegisterRequestValidator).checkConcurrentSimulation(simulations, bindingResult);
        BDDMockito.doNothing().when(simulationRegisterRequestValidator).validate(requestBody, bindingResult);
        given(modelMapper.map(requestBody, SimulationCommand.RegisterSimulationRequest.class)).willReturn(registerCommand);
        BDDMockito.doNothing().when(simulationService).registerSimulation(registerCommand);

        MockHttpServletRequestBuilder requestBuilder = post("/api/v1/users/{userId}/simulations", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));

        // when
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // then
        then(simulationService).should().getSimulations(any());
        then(simulationService).should().registerSimulation(any());

        resultActions
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.message").value("시뮬레이션 생성 요청 확인")
                )
                .andDo(print());
    }

    @Test
    @DisplayName(value = "userId가 없으면 400을 응답한다.")
    void registerSimulationFailTest1() throws Exception {
        // given
        var requestBody = SimulationDto.RegisterSimulationRequest.builder()
//                .userId(userId)
                .assets(Arrays.asList(new AssetCommand("index", "ticker"), new AssetCommand("index", "ticker")))
                .rebalancingPeriod(40)
                .strategies(Arrays.asList(""))
                .build();

        var registerCommand = SimulationCommand.RegisterSimulationRequest.builder().build();

        given(simulationService.getSimulations(any())).willReturn(simulations);
        BDDMockito.doNothing().when(simulationRegisterRequestValidator).checkConcurrentSimulation(simulations, bindingResult);
        BDDMockito.doNothing().when(simulationRegisterRequestValidator).validate(requestBody, bindingResult);
        given(modelMapper.map(requestBody, SimulationCommand.RegisterSimulationRequest.class)).willReturn(registerCommand);
        BDDMockito.doNothing().when(simulationService).registerSimulation(registerCommand);

        MockHttpServletRequestBuilder requestBuilder = post("/api/v1/users/{userId}/simulations", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));

        // when
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // then
        then(simulationService).should().getSimulations(any());
        then(simulationService).should(BDDMockito.times(0)).registerSimulation(any());

        resultActions
                .andExpectAll(
                        status().is4xxClientError()
                )
                .andDo(print());
    }

    @Test
    @DisplayName("진행중인 시뮬레이션이 있으면 202 Accepted를 응답한다.")
    void getSimulationsTest1() throws Exception {
        // given
        List<Strategy> strategies = Arrays.asList(new Strategy("ew", simulations.get(0).getSimulationId()), new Strategy("ew", simulations.get(0).getSimulationId()));

        given(simulationService.getSimulations(any())).willReturn(simulations);
        given(strategyService.findAllBySimulation(any())).willReturn(strategies);

        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/users/{userId}/simulations", userId)
                .contentType(MediaType.APPLICATION_JSON);

        // when
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // then
        then(simulationService).should().getSimulations(any());
        then(strategyService).should(BDDMockito.times(simulations.size())).findAllBySimulation(any());

        resultActions
                .andExpectAll(
                        status().isAccepted()
                )
                .andDo(print());
    }

    @Test
    @DisplayName("모든 시뮬레이션이 완료되면 200을 응답한다.")
    void getSimulationsTest2() throws Exception {
        // given
        simulations.stream().forEach(s -> s.complete());

        var strategies = Arrays.asList(new Strategy("ew", simulations.get(0).getSimulationId()), new Strategy("ew", simulations.get(0).getSimulationId()));

        given(simulationService.getSimulations(any())).willReturn(simulations);
        given(strategyService.findAllBySimulation(any())).willReturn(strategies);

        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/users/{userId}/simulations", userId)
                .contentType(MediaType.APPLICATION_JSON);

        // when
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // then
        then(simulationService).should().getSimulations(any());
        then(strategyService).should(BDDMockito.times(simulations.size())).findAllBySimulation(any());

        resultActions
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }

    @Test
    @DisplayName("시뮬레이션 단건조회 성공시 200을 응답한다.")
    void getSimulationTest1() throws Exception {
        // given
        var simulationId = UUID.randomUUID().toString();
        var foundSimulation = new Simulation(userId, new ArrayList<>(), 0, null, null);
        var strategies = Arrays.asList(new Strategy("ew", simulations.get(0).getSimulationId()), new Strategy("ew", simulations.get(0).getSimulationId()));

        given(simulationService.getSimulation(any())).willReturn(foundSimulation);
        given(strategyService.findAllBySimulation(simulationId)).willReturn(strategies);
        given(simulationStore.store(any())).willReturn(any());

        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/users/{userId}/simulations/{simulationId}", userId, simulationId)
                .contentType(MediaType.APPLICATION_JSON);

        // when
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // then
        then(simulationService).should().getSimulation(any());
        then(strategyService).should().findAllBySimulation(simulationId);
        then(simulationStore).should(BDDMockito.times(0)).store(any());

        resultActions
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }

    @Test
    @DisplayName("시뮬레이션 단건조회시 simulation이 완료되었다면 완료상태를 DB에 저장하고 200을 응답한다.")
    void getSimulationTest2() throws Exception {
        // given
        var simulationId = UUID.randomUUID().toString();
        var foundSimulation = new Simulation(userId, new ArrayList<>(), 0, null, null);
        var strategies = Arrays.asList(new Strategy("ew", simulations.get(0).getSimulationId()), new Strategy("ew", simulations.get(0).getSimulationId()));

        foundSimulation.complete();

        given(simulationService.getSimulation(any())).willReturn(foundSimulation);
        given(strategyService.findAllBySimulation(simulationId)).willReturn(strategies);
        given(simulationStore.store(any())).willReturn(any());

        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/users/{userId}/simulations/{simulationId}", userId, simulationId)
                .contentType(MediaType.APPLICATION_JSON);

        // when
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // then
        then(simulationService).should().getSimulation(any());
        then(strategyService).should().findAllBySimulation(simulationId);
        then(simulationStore).should().store(any());

        resultActions
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }

}