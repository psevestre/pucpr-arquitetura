package br.pucpr.examples.events.reportserver.core.service;

import br.pucpr.examples.events.reportserver.core.domain.ReportJob;
import br.pucpr.examples.events.reportserver.core.ports.DestinationProperties;
import br.pucpr.examples.events.reportserver.core.ports.ReportJobRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportJobRepository reportJobRepository;
    private final RabbitTemplate rabbitTemplate;
    private final DestinationProperties destinationProperties;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public UUID createReportJob( String reportTitle, String reportType) {

        var uuid = UUID.randomUUID();
        var reportJob = new ReportJob();
        reportJob.setReportTitle(reportTitle);
        reportJob.setReportType(reportType);
        reportJob.setJobId(uuid);
        reportJob.setRequestedAt(Instant.now());

        reportJobRepository.save(reportJob);

        var msg = objectMapper.writeValueAsString(reportJob);

        rabbitTemplate.convertAndSend(destinationProperties.getReportJobRoutingKey(), msg);

        return uuid;
    }

    public Optional<ReportJob> findReportJob(UUID jobId) {
        return reportJobRepository.findByJobId(jobId).map( (job) -> job.getCompletedAt() != null ? job : null);
    }

    @RabbitListener(queues = "report.response")
    @SneakyThrows
    public void processReportFinishedEvent(String message) {

        var reportCompleteMessage = objectMapper.readValue(message,ReportCompleteMessage.class);

        log.info("Job finalizado: jobId={}, location={}", reportCompleteMessage.getJobId(), reportCompleteMessage.getReportLocation());

        reportJobRepository.findByJobId(reportCompleteMessage.getJobId()).ifPresent( (job) -> {
            job.setCompletedAt(Instant.now());
            job.setReportLocation(reportCompleteMessage.getReportLocation());
            reportJobRepository.save(job);
        });
    }

    @Data
    @Builder
    static class ReportCompleteMessage {
        private final UUID jobId;
        private final String reportLocation;
    }

}
