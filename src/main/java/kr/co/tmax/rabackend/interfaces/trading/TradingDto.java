package kr.co.tmax.rabackend.interfaces.trading;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class TradingDto {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class RegisterPortfolioResponse {
        private String status;
        private String person_id;
        private String portfolio_id;
    }
}
