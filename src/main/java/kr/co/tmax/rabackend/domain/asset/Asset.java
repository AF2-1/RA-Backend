package kr.co.tmax.rabackend.domain.asset;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@ToString
@Getter
@Document(collection = "assets")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Asset {
    @Id
    private String id;

    @Field("index")
    private String index;

    @Field("ticker")
    private String ticker;

    @Field("name")
    private String name;

    @Field("startDate")
    private LocalDateTime startDate;

    @Field("endDate")
    private LocalDateTime endDate;
}

