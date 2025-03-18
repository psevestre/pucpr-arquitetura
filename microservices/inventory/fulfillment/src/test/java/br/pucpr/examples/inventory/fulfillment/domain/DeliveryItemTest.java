package br.pucpr.examples.inventory.fulfillment.domain;

import static br.pucpr.examples.inventory.fulfillment.domain.DeliveryItemTestSamples.*;
import static br.pucpr.examples.inventory.fulfillment.domain.DeliveryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.pucpr.examples.inventory.fulfillment.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryItem.class);
        DeliveryItem deliveryItem1 = getDeliveryItemSample1();
        DeliveryItem deliveryItem2 = new DeliveryItem();
        assertThat(deliveryItem1).isNotEqualTo(deliveryItem2);

        deliveryItem2.setId(deliveryItem1.getId());
        assertThat(deliveryItem1).isEqualTo(deliveryItem2);

        deliveryItem2 = getDeliveryItemSample2();
        assertThat(deliveryItem1).isNotEqualTo(deliveryItem2);
    }

    @Test
    void deliveryTest() {
        DeliveryItem deliveryItem = getDeliveryItemRandomSampleGenerator();
        Delivery deliveryBack = getDeliveryRandomSampleGenerator();

        deliveryItem.setDelivery(deliveryBack);
        assertThat(deliveryItem.getDelivery()).isEqualTo(deliveryBack);

        deliveryItem.delivery(null);
        assertThat(deliveryItem.getDelivery()).isNull();
    }
}
