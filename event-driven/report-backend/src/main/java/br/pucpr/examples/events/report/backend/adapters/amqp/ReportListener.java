package br.pucpr.examples.events.report.backend.adapters.amqp;

import br.pucpr.examples.events.report.backend.core.ports.NotificationService;
import br.pucpr.examples.events.report.backend.core.service.ReportBuilder;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class ReportListener {

    private Executor executor;
    private final NotificationService notifier;

    @PostConstruct
    void init() {
        executor = Executors.newVirtualThreadPerTaskExecutor();
    }


    @RabbitListener(queues = "report.request")
    public void onReportRequest(String message) {

        Gson gson = new Gson();
        var request = gson.fromJson(message, ReportRequest.class);

        var job = ReportBuilder.builder()
          .jobId(request.getJobId())
          .reportTitle(request.getReportTitle())
          .reportType(request.getReportType())
          .notifier(notifier)
          .build();

        // Despacha job
        executor.execute(job);

    }


    @Data
    public static class ReportRequest {
        private Long id;
        private String jobId;
        private String reportTitle;
        private String reportType;
        private String reportLocation;
    }

}
