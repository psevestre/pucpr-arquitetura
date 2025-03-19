package br.pucpr.examples.events.reportserver.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
public class ReportJob {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID jobId;

    @NotNull
    @Size(min = 1, max = 100)
    private String reportTitle;

    @NotNull
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String reportType;

    @Column(length = 4096, nullable = true)
    private String reportLocation;

    @NotNull
    private Instant requestedAt;

    private Instant completedAt;
}
