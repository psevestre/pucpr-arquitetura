package br.pucpr.examples.inventory.fulfillment;

import br.pucpr.examples.inventory.fulfillment.config.AsyncSyncConfiguration;
import br.pucpr.examples.inventory.fulfillment.config.EmbeddedSQL;
import br.pucpr.examples.inventory.fulfillment.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { FulfillmentApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
