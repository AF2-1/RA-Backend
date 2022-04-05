package kr.co.tmax.rabackend.domain.asset;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AssetCommandTest {

    @Test
    void 자산커맨드_테스트() {
        // given
        AssetCommand noArgsCommand = new AssetCommand();
        AssetCommand allArgsCommand = new AssetCommand("simpleIndex", "simpleTicker");

        // when
        String noArgsCommandIndex = noArgsCommand.getIndex();
        String noArgsCommandTicker = noArgsCommand.getTicker();
        String allArgsCommandIndex = allArgsCommand.getIndex();
        String allArgsCommandTicker = allArgsCommand.getTicker();

        // then
        Assertions.assertThat(noArgsCommandIndex).isNull();
        Assertions.assertThat(noArgsCommandTicker).isNull();
        Assertions.assertThat(allArgsCommandIndex).isEqualTo("simpleIndex");
        Assertions.assertThat(allArgsCommandTicker).isEqualTo("simpleTicker");
    }
}