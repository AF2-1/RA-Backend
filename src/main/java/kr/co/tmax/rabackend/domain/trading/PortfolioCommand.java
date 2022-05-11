package kr.co.tmax.rabackend.domain.trading;

import lombok.*;

public class PortfolioCommand {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterPortfolioCallbackRequest {
        private String portfolioId;
    }

}
