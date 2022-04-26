package kr.co.tmax.rabackend.interfaces.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.tmax.rabackend.domain.asset.AssetCommand;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand;
import kr.co.tmax.rabackend.domain.simulation.SimulationService;
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

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("시뮬레이션 컨트롤러 테스트")
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
    protected ModelMapper modelMapper;

    @MockBean
    protected SimulationRegisterRequestValidator simulationRegisterRequestValidator;

    @MockBean
    protected BindingResult bindingResult; // 스프링MVC에서 자동으로 생성해주는 영역이기 떄문에 모킹불가

    @Autowired
    protected MockMvc mockMvc;

    @Test
    @DisplayName(value = "userId로 simulation들을 조회할 수 있다.")
    void registerSimulationTest() throws Exception {
        // given
        var userId = UUID.randomUUID().toString();

        var simulations = Arrays.asList(new Simulation(), new Simulation(), new Simulation());

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
    void getSimulationsTest() throws Exception {
        // given
        var userId = UUID.randomUUID().toString();

        var simulations = Arrays.asList(new Simulation(), new Simulation(), new Simulation());

        var requestBody = SimulationDto.RegisterSimulationRequest.builder()
//                .userId(userId)
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
        then(simulationService).should(BDDMockito.times(0)).registerSimulation(any());

        resultActions
                .andExpectAll(
                        status().is4xxClientError()
                )
                .andDo(print());
    }
}