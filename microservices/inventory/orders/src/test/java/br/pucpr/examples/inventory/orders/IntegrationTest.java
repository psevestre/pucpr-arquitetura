package br.pucpr.examples.inventory.orders;

import br.pucpr.examples.inventory.orders.config.AsyncSyncConfiguration;
import br.pucpr.examples.inventory.orders.config.EmbeddedSQL;
import br.pucpr.examples.inventory.orders.config.JacksonConfiguration;
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
@SpringBootTest(classes = { OrdersApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
