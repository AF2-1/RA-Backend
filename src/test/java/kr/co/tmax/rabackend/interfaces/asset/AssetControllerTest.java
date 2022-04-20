package kr.co.tmax.rabackend.interfaces.asset;

import kr.co.tmax.rabackend.config.SecurityConfig;
import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetService;
import kr.co.tmax.rabackend.domain.user.User;
import kr.co.tmax.rabackend.exception.ControllerAdvice;
import kr.co.tmax.rabackend.interfaces.alert.AlertService;
import kr.co.tmax.rabackend.security.TokenAuthenticationFilter;
import kr.co.tmax.rabackend.security.TokenProvider;
import kr.co.tmax.rabackend.security.UserPrincipal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
class AssetControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AssetService assetService;

    @MockBean
    ModelMapper modelMapper;

    final AssetDto asset1 = AssetDto.builder()
            .name("simpleName1")
            .ticker("simpleTicker1")
            .index("simpleIndex1")
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now())
            .build();

    final AssetDto asset2 = AssetDto.builder()
            .name("simpleName2")
            .ticker("simpleTicker2")
            .index("simpleIndex2")
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now())
            .build();


    final AssetDto asset3 = AssetDto.builder()
            .name("simpleName3")
            .ticker("simpleTicker3")
            .index("simpleIndex3")
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now())
            .build();


    List<AssetDto> assetList = new ArrayList<>();

    @Test
    void 자산Dto_생성_테스트() {
        // given
        AssetDto noArgsAssetDto = new AssetDto();
        Asset asset = Asset.builder()
                .name("simpleName")
                .build();
        AssetDto assetDto = new AssetDto(asset);

        // when
        String noArgsAssetDtoIndex = noArgsAssetDto.getIndex();
        String name = assetDto.getName();

        // then
        Assertions.assertThat(noArgsAssetDtoIndex).isNull();
        Assertions.assertThat(name).isEqualTo("simpleName");
    }

    @Test
    void 자산_전체조회_컨트롤러_테스트() throws Exception {

        assetList.add(asset1);
        assetList.add(asset2);
        assetList.add(asset3);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/assets")
                        .contentType(MediaType.APPLICATION_JSON));

        given(assetService.findAll().stream().map(AssetDto::new).collect(Collectors.toList())).willReturn(assetList);

        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("전체 자산 조회 성공"));
//                .andExpect(jsonPath("$.data[0].index").value("simpleIndex1"))
//                .andExpect(jsonPath("$.data[0].ticker").value("simpleTicker1"))
//                .andExpect(jsonPath("$.data[1].index").value("simpleIndex2"))
//                .andExpect(jsonPath("$.data[1].ticker").value("simpleTicker2"))
//                .andExpect(jsonPath("$.data[2].index").value("simpleIndex3"))
//                .andExpect(jsonPath("$.data[2].ticker").value("simpleTicker3"));
    }

//    @Test
//    void 자산_티커와인덱스조회_컨트롤러_테스트() throws Exception {
//
//        Mockito.when(modelMapper.map(assetService.searchByTickerAndIndex("simpleTicker1", "simpleIndex1"),
//                AssetDto.class)).thenReturn(asset1);
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders
//                                .get("/api/v1/assets")
//                                .param("ticker", "simpleTicker1")
//                                .param("index", "simpleIndex1")
//                                .accept(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.message").value("티커와 인덱스로 자산 조회 성공"))
//                .andExpect(jsonPath("$.data.index").value("simpleIndex1"))
//                .andExpect(jsonPath("$.data.ticker").value("simpleTicker1"));
//    }
}