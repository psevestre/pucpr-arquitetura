package br.pucpr.examples.events.report.backend.adapters.amqp;

import br.pucpr.examples.events.report.backend.core.ports.NotificationService;
import com.google.errorprone.annotations.Var;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitNotifier implements NotificationService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${report.destinations.response}")
    private String queueName;

    @Override
    public void notifyReportComplete(String jobId, String reportLocation) {

        ReportCompleteMessage message = ReportCompleteMessage.builder()
                .jobId(jobId)
                .reportLocation(reportLocation)
                .build();

        var gson = new Gson();
        var json = gson.toJson(message);

        rabbitTemplate.convertAndSend(queueName, json);

    }

    @Data
    @Builder
    static class ReportCompleteMessage {
        private final String jobId;
        private final String reportLocation;
    }
}
