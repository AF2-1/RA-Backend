package kr.co.tmax.rabackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

@SpringBootTest
class RaBackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void 시간계산() {
        System.out.println(LocalTime.now());
    }
}
