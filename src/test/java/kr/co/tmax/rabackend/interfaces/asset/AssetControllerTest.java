package kr.co.tmax.rabackend.interfaces.asset;

import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class AssetControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AssetService assetService;

    final Asset asset1 = Asset.builder()
            .ticker("simpleTicker1")
            .index("simpleIndex1")
            .build();

    final Asset asset2 = Asset.builder()
            .ticker("simpleTicker2")
            .index("simpleIndex2")
            .build();

    final Asset asset3 = Asset.builder()
            .ticker("simpleTicker3")
            .index("simpleIndex3")
            .build();

    List<Asset> assetList = new ArrayList<>();

    @Test
    void 자산_전체조회_컨트롤러_테스트() throws Exception {

        assetList.add(asset1);
        assetList.add(asset2);
        assetList.add(asset3);

        Mockito.when(assetService.findAll()).thenReturn(assetList);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/assets")
                                .contentType(MediaType.APPLICATION_JSON));

        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("전체 자산 조회 성공"))
                .andExpect(jsonPath("$.data[0].index").value("simpleIndex1"))
                .andExpect(jsonPath("$.data[0].ticker").value("simpleTicker1"))
                .andExpect(jsonPath("$.data[1].index").value("simpleIndex2"))
                .andExpect(jsonPath("$.data[1].ticker").value("simpleTicker2"))
                .andExpect(jsonPath("$.data[2].index").value("simpleIndex3"))
                .andExpect(jsonPath("$.data[2].ticker").value("simpleTicker3"));
    }

    @Test
    void 자산_티커와인덱스조회_컨트롤러_테스트() throws Exception {

        Mockito.when(assetService.searchByTickerAndIndex("simpleTicker1", "simpleIndex1")).thenReturn(asset1);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/assets")
                                .param("ticker", "simpleTicker1")
                                .param("index", "simpleIndex1")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("티커와 인덱스로 자산 조회 성공"))
                .andExpect(jsonPath("$.data.index").value("simpleIndex1"))
                .andExpect(jsonPath("$.data.ticker").value("simpleTicker1"));
    }
}