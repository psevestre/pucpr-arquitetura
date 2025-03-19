package br.pucpr.examples.events.reportserver.adapters.rest;

import br.pucpr.examples.events.reportserver.core.domain.ReportJob;
import br.pucpr.examples.events.reportserver.core.service.ReportService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/report")
    public ResponseEntity<CreateReportResult> createReport(@RequestBody CreateReportRequest request) {

        var uuid = reportService.createReportJob(request.getReportTitle(), request.getReportType());

        var result = CreateReportResult.builder()
          .jobId(uuid)
          .build();

        return ResponseEntity.status(HttpStatus.CREATED)
          .body(result);

    }

    @GetMapping("/report/{jobId}")
    public ResponseEntity<GetReportJobResult> getReport(@PathVariable("jobId") UUID jobId) {

        return reportService.findReportJob(jobId)
            .map(reportJob -> ResponseEntity.ok(toReportResult(reportJob)))
            .orElse(ResponseEntity.status(HttpStatus.TOO_EARLY).build());

    }

    private GetReportJobResult toReportResult(ReportJob reportJob) {
        return GetReportJobResult.builder()
          .jobId(reportJob.getJobId())
          .reportTitle(reportJob.getReportTitle())
          .reportType(reportJob.getReportType())
          .downloadUrl("/report/" + reportJob.getJobId() + "/download")
          .build();

    }

    @Data
    @Builder
    public static class CreateReportResult {
        private UUID jobId;
    }

    @Data
    @Builder
    public static class CreateReportRequest {
        private String reportTitle;
        private String reportType;
    }

    @Data
    @Builder
    public static class GetReportJobResult {
        private UUID jobId;
        private String reportTitle;
        private String reportType;
        private String downloadUrl;
    }

}
