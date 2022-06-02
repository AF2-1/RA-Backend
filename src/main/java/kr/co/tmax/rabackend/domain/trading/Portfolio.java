package kr.co.tmax.rabackend.domain.trading;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Document(collection = "portfolios")
public class Portfolio {
    @Id
    @JsonIgnore
    private ObjectId id;

    private Boolean isExecuted = false;

    private String userId;

    private LocalDateTime createdDate;

    protected Portfolio() {
        this.createdDate = LocalDateTime.now();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private Agent agent;
    private Info info;
    private Order order;
    private Trade trade;

    public void complete() {
        this.isExecuted = true;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Agent {
        private String userId;
        private Float fee;
        private Float slippage;
        private Float initBalance;
        private Float oneStockWeight;
        private int maxHoldNum;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Info {
        private List<String> periods;
        private String universe;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Order {
        private OrderType buyOrder;
        private OrderType sellOrder;
        private OrderType timeOrder;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderType {
        private Float lookback;
        private String ptype;
        private Float tune;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Trade {
        private Buy buy;
        private Sell sell;
        private Priority priority;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Buy {
        private List<Condition> conditions;
        private String logic;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Condition {
        private String name;
        private String factor;
        private Float upper;
        private Float lower;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DetailConditions {
        private String factor;
        private Float upper;
        private Float lower;
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
}
