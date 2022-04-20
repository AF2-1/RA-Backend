package kr.co.tmax.rabackend.external;

import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@Disabled  //TODO: 구현 중
@ExtendWith(MockitoExtension.class)
class KserveApiClientImplTest {

    @InjectMocks
    protected KserveApiClientImpl kserveApiClient;

    @Mock
    protected WebClient webClient;
    @Mock
    protected AppProperties appProperties;

    private AppProperties.Ai aIproperty;
    private Simulation simulation;
    private List<String> strategyNames;

    @BeforeEach
    void setUp() {
        aIproperty = new AppProperties.Ai();
        aIproperty.setUrl("ai-url");
        aIproperty.setPath("ai-path");

        simulation = Simulation.builder()
                .userId(UUID.randomUUID().toString())
                .assets(Arrays.asList(new Asset(), new Asset()))
                .endDate(LocalDate.MIN)
                .startDate(LocalDate.MIN)
                .rebalancingPeriod(40)
                .build();

        strategyNames = Arrays.asList("a", "b", "c");
    }

    @Test
    void requestAASuccessTest() {
        // given
        var requestBodyMock = mock(SimulationDto.RegisterStrategyRequest.class);
        var webClientRequestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        var webClientRequestBodySpecMock = mock(WebClient.RequestBodySpec.class);
        var webClientRequestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        var webClientResponseSpecMock = mock(WebClient.ResponseSpec.class);
        var monoMock = mock(Mono.class);
        var simulationDtoRegisterStrategyResponseMock = mock(SimulationDto.RegisterStrategyResponse.class);

        given(appProperties.getAi()).willReturn(aIproperty);

        given(webClient.post()).willReturn(webClientRequestBodyUriSpecMock);
        given(webClientRequestBodyUriSpecMock.uri(aIproperty.getPath())).willReturn(webClientRequestBodySpecMock);
        given(webClientRequestBodySpecMock.contentType(MediaType.APPLICATION_JSON)).willReturn(webClientRequestBodySpecMock);
        given(webClientRequestBodySpecMock.bodyValue(requestBodyMock)).willReturn(webClientRequestHeadersSpecMock);
        given(webClientRequestHeadersSpecMock.retrieve()).willReturn(webClientResponseSpecMock);
        given(webClientResponseSpecMock.bodyToMono(SimulationDto.RegisterStrategyResponse.class)).willReturn(monoMock);
        given(monoMock.block()).willReturn(simulationDtoRegisterStrategyResponseMock);

        // when
        kserveApiClient.requestAA(simulation, strategyNames);

        // then
        then(appProperties).should(BDDMockito.times(strategyNames.size())).getAi();
    }
}