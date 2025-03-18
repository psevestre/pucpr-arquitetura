package br.pucpr.examples.inventory.fulfillment.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DeliveryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Delivery getDeliverySample1() {
        return new Delivery()
            .id(1L)
            .orderId("orderId1")
            .customerId("customerId1")
            .name("name1")
            .email("email1")
            .addressLine1("addressLine11")
            .addressLine2("addressLine21")
            .zipCode("zipCode1")
            .city("city1")
            .country("country1")
            .deliveryInstructions("deliveryInstructions1");
    }

    public static Delivery getDeliverySample2() {
        return new Delivery()
            .id(2L)
            .orderId("orderId2")
            .customerId("customerId2")
            .name("name2")
            .email("email2")
            .addressLine1("addressLine12")
            .addressLine2("addressLine22")
            .zipCode("zipCode2")
            .city("city2")
            .country("country2")
            .deliveryInstructions("deliveryInstructions2");
    }

    public static Delivery getDeliveryRandomSampleGenerator() {
        return new Delivery()
            .id(longCount.incrementAndGet())
            .orderId(UUID.randomUUID().toString())
            .customerId(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .addressLine1(UUID.randomUUID().toString())
            .addressLine2(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .deliveryInstructions(UUID.randomUUID().toString());
    }
}
