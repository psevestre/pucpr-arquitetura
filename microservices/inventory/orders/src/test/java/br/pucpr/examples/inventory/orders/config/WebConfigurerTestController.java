package br.pucpr.examples.inventory.orders.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebConfigurerTestController {

    @GetMapping("/api/test-cors")
    public void testCorsOnApiPath() {
        // empty method
    }

    @GetMapping("/test/test-cors")
    public void testCorsOnOtherPath() {
        // empty method
    }
}
