package br.pucpr.examples.inventory.fulfillment.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DeliveryItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DeliveryItem getDeliveryItemSample1() {
        return new DeliveryItem().id(1L).sku("sku1").description("description1").quantity(1);
    }

    public static DeliveryItem getDeliveryItemSample2() {
        return new DeliveryItem().id(2L).sku("sku2").description("description2").quantity(2);
    }

    public static DeliveryItem getDeliveryItemRandomSampleGenerator() {
        return new DeliveryItem()
            .id(longCount.incrementAndGet())
            .sku(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet());
    }
}
