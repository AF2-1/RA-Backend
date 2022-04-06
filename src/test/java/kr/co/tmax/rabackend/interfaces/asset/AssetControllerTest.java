package kr.co.tmax.rabackend.interfaces.asset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AssetControllerTest {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void before() {
        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(AssetControllerTest.class)
                        .alwaysExpect(status().isOk())
                        .build();
    }

    @Test
    void 자산전체조회_컨트롤러_테스트() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/assets")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void 자산티커인덱스로_컨트롤러_테스트() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/assets")
                                .param("ticker", "033780.KS")
                                .param("index", "KOSPI")
                                .accept(MediaType.APPLICATION_JSON)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", "전체 자산 조회 성공"))
                .andExpect(jsonPath("$.data.name", ""));
    }
}