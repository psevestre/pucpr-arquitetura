package br.pucpr.examples.events.report.backend.core.service;

import br.pucpr.examples.events.report.backend.core.ports.NotificationService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Builder
public class ReportBuilder implements Runnable{

    private final String jobId;
    private final String reportType;
    private final String reportTitle;
    private final NotificationService notifier;

    @SneakyThrows
    public void run() {

        // Simula um processo bem longo
        log.info("Gerando relatório:  jobId={}, reportType={}, reportTitle={}", jobId, reportType, reportTitle);
        Thread.sleep(5000);


        // Simula o caminho em que o relatório seria gerado
        var location =  UUID.randomUUID().toString() + ".pdf";

        log.info("Relatório gerado ! jobId={}, location={}", jobId, location);

        notifier.notifyReportComplete(jobId, location);

    }
}
