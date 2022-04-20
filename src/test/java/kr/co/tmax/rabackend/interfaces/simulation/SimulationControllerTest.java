package kr.co.tmax.rabackend.interfaces.simulation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.tmax.rabackend.config.AppConfig;
import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.config.SecurityConfig;
import kr.co.tmax.rabackend.config.WebMvcConfig;
import kr.co.tmax.rabackend.domain.asset.AssetCommand;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand;
import kr.co.tmax.rabackend.domain.simulation.SimulationService;
import kr.co.tmax.rabackend.domain.strategy.StrategyService;
import kr.co.tmax.rabackend.exception.ControllerAdvice;
import kr.co.tmax.rabackend.infrastructure.user.UserRepository;
import kr.co.tmax.rabackend.security.TokenAuthenticationFilter;
import kr.co.tmax.rabackend.security.oauth2.CustomOAuth2UserService;
import kr.co.tmax.rabackend.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import kr.co.tmax.rabackend.security.oauth2.OAuth2AuthenticationFailureHandler;
import kr.co.tmax.rabackend.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcExtensionsKt;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Disabled  //TODO: 구현 중
@DisplayName("시뮬레이션 컨트롤러 테스트")
//@AutoConfigureMockMvc
//@ContextConfiguration(classes = SimulationController.class)
@WebMvcTest(value = SimulationController.class, useDefaultFilters = false) // 이 어노테이션은 filter, WebSecurityConfigurerAdapter 등을 포함시킴
@AutoConfigureMockMvc(addFilters = false)
//@ContextHierarchy({
//        @ContextConfiguration(classes = {AppConfig.class, AppProperties.class/*, SecurityConfig.class*/, WebMvcConfig.class})
//})
class SimulationControllerTest {

//    @Configuration
//    static class Config{
//    }

    @Configuration
    class SecurityConfig extends WebSecurityConfigurerAdapter{

    }

    @Autowired
    protected ObjectMapper objectMapper;

//    private static final ObjectMapper objectMapper = JsonUtil.getJackson();

//    @Autowired
//    protected SimulationController simulationController;

    @MockBean
    protected SimulationService simulationService;

    @MockBean
    protected StrategyService strategyService;

    @MockBean
    protected ModelMapper modelMapper;

    @MockBean
    protected SimulationRegisterRequestValidator simulationRegisterRequestValidator;
//
//    @MockBean
//    BindingResult bindingResultMock;
//
//    @MockBean
//    UriComponentsBuilder uriComponentsBuilderMock;

//
//    @Autowired
//    protected CustomOAuth2UserService customOAuth2UserService;
//    @Autowired
//    protected OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
//    @Autowired
//    protected OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
//    @Autowired
//    protected HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
//    @Autowired
//    protected TokenAuthenticationFilter tokenAuthenticationFilter;
//    @Autowired
//    protected UserRepository userRepository;
//    @Autowired
//    protected JpaRepository jpaRepository;


    @Autowired
    protected MockMvc mockMvc;

//    @BeforeEach
//    void setUp(){
//        mockMvc = MockMvcBuilders
//                .standaloneSetup(simulationController)
////                .standaloneSetup(new SimulationController(null, null, null, null))
////                .setControllerAdvice(new ControllerAdvice())
//                .build();
//    }

    @Test
    @WithMockUser
    @DisplayName(value = "시뮬레이션을 등록할 수 있다.")
    void registerSimulationTest() throws Exception {
        // given
        var userId = UUID.randomUUID().toString();
////
        given(simulationService.getSimulations(any())).willReturn(Arrays.asList(new Simulation(), new Simulation(), new Simulation()));  //stub
        BDDMockito.doNothing().when(simulationRegisterRequestValidator).checkConcurrentSimulation(any(), any());
        BDDMockito.doNothing().when(simulationRegisterRequestValidator).validate(any(), any());
//        given(bindingResultMock.hasErrors()).willReturn(false);
        given(modelMapper.map(any(), any())).willReturn(SimulationCommand.RegisterSimulationRequest.builder().build());
        BDDMockito.doNothing().when(simulationService).registerSimulation(any());


        SimulationDto.RegisterSimulationRequest requestBody = SimulationDto.RegisterSimulationRequest.builder()
                .userId(userId)
                .assets(Arrays.asList(new AssetCommand("index", "ticker"), new AssetCommand("index", "ticker")))
                .rebalancingPeriod(40)
                .strategies(Arrays.asList(""))
                .build();

        MockHttpServletRequestBuilder requestBuilder = post("/api/v1/users/{userId}/simulations", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));

        // when
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // then
        then(simulationService).should().getSimulations(any());
        then(simulationRegisterRequestValidator).should(BDDMockito.times(0)).checkConcurrentSimulation(any(), any());
        then(simulationRegisterRequestValidator).should(BDDMockito.times(0)).validate(any(), any());
//        then(bindingResultMock).should(BDDMockito.times(1)).hasErrors();
        then(modelMapper).should(times(1)).map(any(), SimulationCommand.RegisterSimulationRequest.class);
        then(simulationService).should(times(0)).registerSimulation(any());

        resultActions
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").value("시뮬레이션 생성 요청 확인")
                )
                .andDo(print());
    }

    @Test
    @DisplayName(value = "userId로 simulation들을 조회할 수 있다.")
    void getSimulationsTest() throws Exception {
        // given
        List<Simulation> simulations = Arrays.asList(new Simulation(), new Simulation(), new Simulation());
        simulations.stream().forEach(s -> s.complete());

        given(simulationService.getSimulations(any())).willReturn(simulations);

        given(strategyService.findAllBySimulation(any())).willReturn(any()); // List<Strategy>
        var simpleSimulationResponseMock = mock(SimulationDto.SimpleSimulationResponse.class);
        given(simpleSimulationResponseMock.create(any(), any())).willReturn(any());


        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/users/{userId}/simulations", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON);

        // when

        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//                .andExpect(jsonPath("$.message").value());
    }
}