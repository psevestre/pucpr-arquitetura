package br.pucpr.examples.events.report.backend.core.ports;

public interface NotificationService {
    void notifyReportComplete(String jobId, String reportLocation);
}
