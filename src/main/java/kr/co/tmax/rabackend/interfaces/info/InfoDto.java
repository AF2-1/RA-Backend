package kr.co.tmax.rabackend.interfaces.info;

import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class InfoDto {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class KeywordRequest {
        private EnKeywordCrawlingConfig en_keyword_crawling_config;
        private EnKeywordModuleConfig en_keyword_module_config;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class EnKeywordCrawlingConfig {
        private String target_market;
        private String start_date;
        private String end_date;
        private int last_page;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class EnKeywordModuleConfig {
        private String mod;
        private int top_n;
        private int top_k;
        private double diversity;
    }
}
