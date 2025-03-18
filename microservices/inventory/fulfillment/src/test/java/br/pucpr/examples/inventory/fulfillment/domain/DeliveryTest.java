package br.pucpr.examples.inventory.fulfillment.domain;

import static br.pucpr.examples.inventory.fulfillment.domain.DeliveryItemTestSamples.*;
import static br.pucpr.examples.inventory.fulfillment.domain.DeliveryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.pucpr.examples.inventory.fulfillment.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DeliveryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Delivery.class);
        Delivery delivery1 = getDeliverySample1();
        Delivery delivery2 = new Delivery();
        assertThat(delivery1).isNotEqualTo(delivery2);

        delivery2.setId(delivery1.getId());
        assertThat(delivery1).isEqualTo(delivery2);

        delivery2 = getDeliverySample2();
        assertThat(delivery1).isNotEqualTo(delivery2);
    }

    @Test
    void itemsTest() {
        Delivery delivery = getDeliveryRandomSampleGenerator();
        DeliveryItem deliveryItemBack = getDeliveryItemRandomSampleGenerator();

        delivery.addItems(deliveryItemBack);
        assertThat(delivery.getItems()).containsOnly(deliveryItemBack);
        assertThat(deliveryItemBack.getDelivery()).isEqualTo(delivery);

        delivery.removeItems(deliveryItemBack);
        assertThat(delivery.getItems()).doesNotContain(deliveryItemBack);
        assertThat(deliveryItemBack.getDelivery()).isNull();

        delivery.items(new HashSet<>(Set.of(deliveryItemBack)));
        assertThat(delivery.getItems()).containsOnly(deliveryItemBack);
        assertThat(deliveryItemBack.getDelivery()).isEqualTo(delivery);

        delivery.setItems(new HashSet<>());
        assertThat(delivery.getItems()).doesNotContain(deliveryItemBack);
        assertThat(deliveryItemBack.getDelivery()).isNull();
    }
}
