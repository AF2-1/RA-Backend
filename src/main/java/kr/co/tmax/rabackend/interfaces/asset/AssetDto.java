package kr.co.tmax.rabackend.interfaces.asset;

import kr.co.tmax.rabackend.domain.asset.Asset;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssetDto {
    private String index;
    private String ticker;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public AssetDto(Asset asset) {
        this.index = asset.getIndex();
        this.ticker = asset.getTicker();
        this.name = asset.getName();
        this.startDate = asset.getStartDate();
        this.endDate = asset.getEndDate();
    }
}
