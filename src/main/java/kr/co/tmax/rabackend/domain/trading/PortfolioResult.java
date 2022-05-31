package kr.co.tmax.rabackend.domain.trading;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Document(collection = "portfolio_results")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioResult {
    @Id
    @JsonIgnore
    private ObjectId id;

    @NotNull
    private Boolean isSuccess;
    @NotNull
    private String portfolioId;
    private List<AccountHistory> accountHistory;
    private List<OrderHistory> orderHistory;
    private Summary summary;
    private Summary1 summary1;
    private Summary2 summary2;
    private Summary3 summary3;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AccountHistory {
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime timestamp;
        private String level;
        private String loggerName;
        private String module;
        private String method;
        private String portfolioId;
        private Long personId;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime simDate;
        private Double initBalance;
        private Double totalAsset;
        private Double cash;
        private Map<String, Investment> investments;

        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        public static class Investment {
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
            private LocalDateTime orderDate;
            private Long orderPrice;
            private Long quantity;
            private Double orderValue;
            private Long currentValue;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderHistory {
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime timestamp;
        private String level;
        private String loggerName;
        private String module;
        private String method;
        private Long orderId;
        private String portfolioId;
        private String tactics;
        private String ticker;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime simDate;
        private Long price;
        private Long quantity;
        private Long amount;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Summary {
        private Agent agent;
        private Info info;
        private Order order;
        private Trade trade;

        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        public static class Info {
//            @DateTimeFormat(pattern = "yyyyMMdd")
//            private List<LocalDateTime> periods;
            private String universe;
        }

        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        public static class Order {
            private OrderInfo buyOrder;
            private OrderInfo sellOrder;
            private OrderInfo timeOrder;

            @Getter
            @NoArgsConstructor(access = AccessLevel.PROTECTED)
            public static class OrderInfo {
                private Integer lookback;
                private String ptype;
                private Double tune;
            }
        }

        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        public static class Trade {
            private Buy buy;
            private Sell sell;
            private Priority priority;

            @Getter
            @NoArgsConstructor(access = AccessLevel.PROTECTED)
            public static class Buy {
                private List<Condition> conditions;
                private String logic;
            }

            @Getter
            @NoArgsConstructor(access = AccessLevel.PROTECTED)
            public static class Sell {
                private List<Condition> conditions;
                private String logic;
                private List<Integer> timeCuts;
            }

            @Getter
            @NoArgsConstructor(access = AccessLevel.PROTECTED)
            public static class Priority {
                private String condition;
                private String order;
            }

            @Getter
            @NoArgsConstructor(access = AccessLevel.PROTECTED)
            public static class Condition {
                private String name;
                private String factor;
                private Double upper;
                private Double lower;
            }
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Summary1 {
        private Double totalValue;
        private Map<String, Double> ratio;
        private Map<String, Double> perValue;
        private DailyNav dailyNav;

        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        public static class DailyNav {
            private List<String> period;  // TODO: List<LocalDateTime>을 메시지컨버터를 통해 deserialize
            private List<Double> nav;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Summary2 {
//        private SummaryInfo mySimulation;
//        private SummaryInfo benchmark;

        private Map<String, Double> mySimulation;
        private Map<String, Double> benchmark;

//        @Getter
//        @NoArgsConstructor(access = AccessLevel.PROTECTED)
//        public static class SummaryInfo {   // TODO: CR, CAGR, MDD 이 null로 들어오는 문제로 인해 임시로 Map으로 구현
//            private Double CR;
//            private Double CAGR;
//            private Double MDD;
//            private Double sharpeRatio;
//        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Summary3 {
        // TODO
    }
}
