package kr.co.tmax.rabackend.interfaces.trading;

import lombok.*;

public class TradingDto {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class RegisterPortfolioCallbackRequest {
        private String status;
        private String person_id;
        private String portfolio_id;
    }
}
