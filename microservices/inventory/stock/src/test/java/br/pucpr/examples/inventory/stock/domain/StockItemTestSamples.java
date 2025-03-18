package br.pucpr.examples.inventory.stock.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StockItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StockItem getStockItemSample1() {
        return new StockItem().id(1L).sku("sku1").description("description1").available(1).reserved(1).minStock(1);
    }

    public static StockItem getStockItemSample2() {
        return new StockItem().id(2L).sku("sku2").description("description2").available(2).reserved(2).minStock(2);
    }

    public static StockItem getStockItemRandomSampleGenerator() {
        return new StockItem()
            .id(longCount.incrementAndGet())
            .sku(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .available(intCount.incrementAndGet())
            .reserved(intCount.incrementAndGet())
            .minStock(intCount.incrementAndGet());
    }
}
