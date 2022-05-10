package kr.co.tmax.rabackend.interfaces.trading;

import lombok.*;

public class TradingDtoWithEngine {

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
