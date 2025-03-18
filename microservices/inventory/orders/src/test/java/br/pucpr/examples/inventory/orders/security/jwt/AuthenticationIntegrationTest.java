package br.pucpr.examples.inventory.orders.security.jwt;

import br.pucpr.examples.inventory.orders.config.SecurityConfiguration;
import br.pucpr.examples.inventory.orders.config.SecurityJwtConfiguration;
import br.pucpr.examples.inventory.orders.config.WebConfigurer;
import br.pucpr.examples.inventory.orders.management.SecurityMetersService;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import tech.jhipster.config.JHipsterProperties;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        JHipsterProperties.class,
        WebConfigurer.class,
        SecurityConfiguration.class,
        SecurityJwtConfiguration.class,
        SecurityMetersService.class,
        JwtAuthenticationTestUtils.class,
    }
)
public @interface AuthenticationIntegrationTest {
}
