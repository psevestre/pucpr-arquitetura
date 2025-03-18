package br.pucpr.examples.inventory.orders.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderCustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderCustomer getOrderCustomerSample1() {
        return new OrderCustomer()
            .id(1L)
            .customerId("customerId1")
            .name("name1")
            .email("email1")
            .addressLine1("addressLine11")
            .addressLine2("addressLine21")
            .zipCode("zipCode1")
            .city("city1")
            .country("country1");
    }

    public static OrderCustomer getOrderCustomerSample2() {
        return new OrderCustomer()
            .id(2L)
            .customerId("customerId2")
            .name("name2")
            .email("email2")
            .addressLine1("addressLine12")
            .addressLine2("addressLine22")
            .zipCode("zipCode2")
            .city("city2")
            .country("country2");
    }

    public static OrderCustomer getOrderCustomerRandomSampleGenerator() {
        return new OrderCustomer()
            .id(longCount.incrementAndGet())
            .customerId(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .addressLine1(UUID.randomUUID().toString())
            .addressLine2(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString());
    }
}
