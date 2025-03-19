package br.pucpr.examples.events.reportserver.core.ports;

import br.pucpr.examples.events.reportserver.core.domain.ReportJob;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReportJobRepository extends CrudRepository<ReportJob, Long> {
    Optional<ReportJob> findByJobId(UUID jobId);
}
