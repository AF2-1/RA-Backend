package kr.co.tmax.rabackend.domain.trading;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@ToString
@Getter
@Setter
@Document(collection = "portfolios")
@NoArgsConstructor
@AllArgsConstructor
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
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Agent {
        private String userId;
        private Float fee;
        private Float slippage;
        private Float initBalance;
        private Float oneStockWeight;
        private int maxHoldNum;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Info {
        private List<String> periods;
        private String universe;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Order {
        private OrderType buyOrder;
        private OrderType sellOrder;
        private OrderType timeOrder;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class OrderType {
        private Float lookback;
        private String ptype;
        private Float tune;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Trade {
        private Buy buy;
        private Sell sell;
        private Priority priority;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Buy {
        private List<Condition> conditions;
        private String logic;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Condition {
        private String name;
        private String factor;
        private Float upper;
        private Float lower;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class DetailConditions {
        private String factor;
        private Float upper;
        private Float lower;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Sell {
        private List<Condition> conditions;
        private String logic;
        private List<Integer> timeCuts;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Priority {
        private String condition;
        private String order;
    }
}
