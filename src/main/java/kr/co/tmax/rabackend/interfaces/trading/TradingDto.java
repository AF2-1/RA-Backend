package kr.co.tmax.rabackend.interfaces.trading;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TradingDto {

    @Getter
    @NoArgsConstructor
    public static class RegisterPortfolioResultRequest {
        @NotNull
        private Boolean isSuccess;
        private String portfolioId;
        private List<AccountHistory> accountHistory;
        private List<OrderHistory> orderHistory;
        private Summary1 summary;
        private Summary1 summary1;
        private Summary2 summary2;
        private Summary3 summary3;

        @Getter
        @NoArgsConstructor
        public static class AccountHistory {
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            private LocalDateTime timestamp;
            private String level;
            private String loggerName;
            private String module;
            private String method;
            private String portfolioId;
            private Long personId;
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            private LocalDateTime simDate;
            private Double initBalance;
            private Double totalAsset;
            private Double cash;
            private Map<String, Investment> investments;

            @Getter
            @NoArgsConstructor
            public static class Investment {
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                private LocalDateTime orderDate;
                private Long orderPrice;
                private Long quantity;
                private Double orderValue;
                private Long currentValue;
            }
        }

        @Getter
        @NoArgsConstructor
        public static class OrderHistory {
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            private LocalDateTime timestamp;
            private String level;
            private String loggerName;
            private String module;
            private String method;
            private Long orderId;
            private String portfolioId;
            private String tactics;
            private String ticker;
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            private LocalDateTime simDate;
            private Long price;
            private Long quantity;
            private Long amount;
        }

        @Getter
        @NoArgsConstructor
        public static class Summary1 {
            private Agent agent;
            private Info info;
            private Order order;
            private Trace trace;

            @Getter
            @NoArgsConstructor
            public static class Agent {
                private Long userId;
                private Double fee;
                private Integer slippage;
                private Long initBalance;
                private Double oneStockWeight;
                private Long maxHoldNum;
                private String portfolioId;
            }

            @Getter
            @NoArgsConstructor
            public static class Info {
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                private List<LocalDateTime> periods;
                private String universe;
            }

            @Getter
            @NoArgsConstructor
            public static class Order {
                private OrderInfo buyOrder;
                private OrderInfo sellOrder;
                private OrderInfo timeOrder;

                @Getter
                @NoArgsConstructor
                public static class OrderInfo {
                    private Integer lookback;
                    private String ptype;
                    private Double tune;
                }
            }

            @Getter
            @NoArgsConstructor
            public static class Trace {
                private Buy buy;
                private Sell sell;
                private Priority priority;

                @Getter
                @NoArgsConstructor
                public static class Buy {
                    private List<Condition> conditions;
                    private String logic;
                }

                @Getter
                @NoArgsConstructor
                public static class Sell {
                    private List<Condition> conditions;
                    private String logic;
                    private List<Integer> timeCuts;
                }

                @Getter
                @NoArgsConstructor
                public static class Priority {
                    private String condition;
                    private String order;
                }

                @Getter
                @NoArgsConstructor
                public static class Condition {
                    private String name;
                    private String factor;
                    private Double upper;
                    private Double lower;
                }
            }
        }

        @Getter
        @NoArgsConstructor
        public static class Summary2 {
            private SummaryInfo mySimulation;
            private SummaryInfo benchmark;

            @Getter
            @NoArgsConstructor
            public static class SummaryInfo {
                private Double CR;
                private Double CAGR;
                private Double MDD;
                private Double sharpeRatio;
            }
        }

        @Getter
        @NoArgsConstructor
        public static class Summary3 {
            // TODO
        }

    }
}
