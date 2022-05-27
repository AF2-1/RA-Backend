package kr.co.tmax.rabackend.domain.trading;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Getter
@Document(collection = "portfolios")
@NoArgsConstructor
public class Portfolio {
    @Id
    @JsonIgnore
    private ObjectId id;

    @JsonIgnore
    private Boolean isExcuted = false;

    private Agent agent;
    private Info info;
    private Order order;
    private Trade trade;

    public void complete() {
        this.isExcuted = true;
    }

    @Getter
    @NoArgsConstructor
    public static class Agent {
        private String userId;
        private Float fee;
        private Float slippage;
        private Float initBalance;
        private Float oneStockWeight;
        private int maxHoldNum;
    }

    @Getter
    @NoArgsConstructor
    public static class Info {
        private List<String> periods;
        private String universe;
    }

    @Getter
    @NoArgsConstructor
    public static class Order {
        private OrderType buyOrder;
        private OrderType sellOrder;
        private OrderType timeOrder;
    }

    @Getter
    @NoArgsConstructor
    public static class OrderType {
        private Float lookback;
        private String ptype;
        private Float tune;
    }

    @Getter
    @NoArgsConstructor
    public static class Trade {
        private Buy buy;
        private Sell sell;
        private Priority priority;
    }

    @Getter
    @NoArgsConstructor
    public static class Buy {
        private List<Condition> conditions;
        private String logic;
    }

    @Getter
    @NoArgsConstructor
    public static class Condition {
        private String name;
        private String factor;
        private Float upper;
        private Float lower;
    }

    @Getter
    @NoArgsConstructor
    public static class DetailConditions {
        private String factor;
        private Float upper;
        private Float lower;
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
}
