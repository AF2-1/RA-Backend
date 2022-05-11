package kr.co.tmax.rabackend.interfaces.trading;

import lombok.*;

public class TradingDto {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class RegisterPortfolioCallbackRequest {
        private int status;
    }
}
