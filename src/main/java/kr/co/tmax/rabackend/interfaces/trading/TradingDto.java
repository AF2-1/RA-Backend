package kr.co.tmax.rabackend.interfaces.trading;

import lombok.*;

public class TradingDto {

    @Getter
    @NoArgsConstructor
    public static class RegisterPortfolioCallbackRequest {
        private Boolean isSuccess;
        private String resultUrl;
    }
}
