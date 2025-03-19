package br.pucpr.examples.events.reportserver.core.ports;

public interface DestinationProperties {
    String getReportJobRoutingKey();
    String getReportCompleteQueue();
}
