package kr.co.tmax.rabackend.interfaces.alert;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import io.sentry.Sentry;
import kr.co.tmax.rabackend.interfaces.validation.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
@Slf4j
public class AlertService {
    @Value(value = "${slack.token}")
    String token;

    @Value(value = "${slack.channel.monitor}")
    String monitorChannel;

    @Value(value = "${slack.channel.error}")
    String errorChannel;

    @Value(value = "${slack.channel.test}")
    String testChannel;

    @Value(value = "${slack.channel.etc}")
    String etcChannel;

    public void slackSendMessage(SlackChannel slackChannel, String message){
        try{
            Slack slack = Slack.getInstance();
            String channel;
            if (slackChannel.equals(SlackChannel.ERROR)){
                channel = errorChannel;
            }
            else if (slackChannel.equals(SlackChannel.MONITOR)) {
                channel = monitorChannel;
            }
            else if (slackChannel.equals(SlackChannel.ETC)) {
                channel = etcChannel;
            }
            else {
                channel = testChannel;
            }
            slack.methods(token).chatPostMessage(req -> req.channel(channel).text(message));
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }

    public void slackSendMessage(String message){
        try{
            Slack slack = Slack.getInstance();
            slack.methods(token).chatPostMessage(req -> req.channel(monitorChannel).text(message));
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }

    public void sentryWithSlackMessage(SlackChannel slackChannel, Exception e, ValidationResult v){
        Sentry.captureException(e);
        slackSendMessage(slackChannel, v.toString());
    }

    public void sentryWithSlackMessage(SlackChannel slackChannel, Exception e){
        Sentry.captureException(e);
        slackSendMessage(slackChannel, e.getMessage());
    }
}
