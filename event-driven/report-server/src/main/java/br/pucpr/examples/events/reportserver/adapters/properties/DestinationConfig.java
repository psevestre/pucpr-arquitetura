package br.pucpr.examples.events.reportserver.adapters.properties;

import br.pucpr.examples.events.reportserver.core.ports.DestinationProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ DestinationConfig.SpringDestinationProperties.class})
public class DestinationConfig {


    @ConfigurationProperties(prefix="report.destinations")
    @Data
    public static class SpringDestinationProperties implements DestinationProperties {

        private String reportJobRoutingKey = "report.request";
        private String reportCompleteQueue = "report.complete";

    }
}
