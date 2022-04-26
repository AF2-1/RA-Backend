package kr.co.tmax.rabackend.interfaces.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand;
import kr.co.tmax.rabackend.domain.simulation.SimulationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Strategy 컨트롤러 테스트는")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StrategyControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected StrategyController strategyController;

    @MockBean
    protected CompleteStrategyRequestValidator completeStrategyRequestValidator;

    @MockBean
    protected SimulationService simulationService;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    @DisplayName("AI서버로부터 전략 완료 요청이 도착하면 200을 응답한다.")
    void completeStrategyTest() throws Exception {
        // given
        BDDMockito.doNothing().when(completeStrategyRequestValidator).validate(any(), any());
        BDDMockito.doNothing().when(simulationService).completeStrategy(any(SimulationCommand.CompleteStrategyRequest.class));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("simulationId", UUID.randomUUID().toString());
        params.add("strategyName", "ew");

        MockHttpServletRequestBuilder requestBuilder = post("/api/v1/simulation/callback")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getRequestBody());

        // when
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // then
        then(completeStrategyRequestValidator).should().validate(any(), any());
        then(simulationService).should().completeStrategy(any(SimulationCommand.CompleteStrategyRequest.class));

        resultActions
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }

    private String getRequestBody() {
        return "{\n" +
                "  \"dailyPfValues\": {\n" +
                "    \"2022-03-15\": 1.1,\n" +
                "    \"2022-03-15\": 1.1,\n" +
                "    \"2022-03-15\": 1.1\n" +
                "  },\n" +
                "  \"dailyPfWeights\": {\n" +
                "    \"2022-03-15\": [\n" +
                "      1.1,1.1,1.1\n" +
                "    ],\n" +
                "    \"2022-03-15\": [\n" +
                "      1.1,1.1,1.1\n" +
                "    ],\n" +
                "    \"2022-03-15\": [\n" +
                "      1.1,1.1,1.1\n" +
                "    ]\n" +
                "  },\n" +
                "  \"evaluationResults\": {\n" +
                "    \"cagr\": 1.1,\n" +
                "    \"mdd\": 1.1,\n" +
                "    \"sharpeRatio\": 1.1,\n" +
                "    \"sortinoRatio\": 1.1,\n" +
                "    \"totalReturn\": 1.1,\n" +
                "    \"turnover\": 1.1,\n" +
                "    \"volatility\": 1.1\n" +
                "  },\n" +
                "  \"inferenceResults\": {\n" +
                "    \"2022-03-15\": [\n" +
                "      1.1,1.1,1.1\n" +
                "    ],\n" +
                "    \"2022-03-15\": [\n" +
                "      1.1,1.1,1.1\n" +
                "    ],\n" +
                "    \"2022-03-15\": [\n" +
                "      1.1,1.1,1.1\n" +
                "    ]\n" +
                "  },\n" +
                "  \"model\": {\n" +
                "    \"assets\": [\n" +
                "      \"SPX\"\n" +
                "    ],\n" +
                "    \"trainedTime\": \"2022-03-14\"\n" +
                "  },\n" +
                "  \"recommendedPf\": [\n" +
                "    1.1\n" +
                "  ]\n" +
                "}";
    }
}