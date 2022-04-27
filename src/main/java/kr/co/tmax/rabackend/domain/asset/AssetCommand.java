package kr.co.tmax.rabackend.domain.asset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AssetCommand {

    private String index;
    private String ticker;
}
