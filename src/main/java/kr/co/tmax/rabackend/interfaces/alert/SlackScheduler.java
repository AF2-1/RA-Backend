package kr.co.tmax.rabackend.interfaces.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class SlackScheduler {
    private final AlertService alertService;
//    @Scheduled(cron="0 0/1 * * * *") //1분
//    public void test() {
//        alertService.slackSendMessage(SlackChannel.MONITOR, "hi");
//    }
//
//    @Scheduled(cron="0 0/1 * * * *") //1분
//    public void alarmDinner() {
//        if (LocalTime.now().isBefore(LocalTime.of(16,50,59)) && LocalTime.now().isAfter(LocalTime.of(16,49,59))){
//            alertService.slackSendMessage(SlackChannel.ETC, "운동 10분전 입니다");
//        }
//
//        else if (LocalTime.now().isBefore(LocalTime.of(17,50,59)) && LocalTime.now().isAfter(LocalTime.of(17,49,59))){
//            alertService.slackSendMessage(SlackChannel.ETC, "곧 저녁 식사시간 입니다. 오늘의 메뉴는 ~");
//        }
//    }
}
