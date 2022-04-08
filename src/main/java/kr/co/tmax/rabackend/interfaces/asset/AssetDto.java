package kr.co.tmax.rabackend.interfaces.asset;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import kr.co.tmax.rabackend.domain.asset.Asset;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDto {
    private String index;
    private String ticker;
    private String name;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endDate;

    public AssetDto(Asset asset) {
        this.index = asset.getIndex();
        this.ticker = asset.getTicker();
        this.name = asset.getName();
        this.startDate = asset.getStartDate();
        this.endDate = asset.getEndDate();
    }
}
