package br.pucpr.examples.inventory.orders.domain;

import static br.pucpr.examples.inventory.orders.domain.OrderCustomerTestSamples.*;
import static br.pucpr.examples.inventory.orders.domain.OrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.pucpr.examples.inventory.orders.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderCustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderCustomer.class);
        OrderCustomer orderCustomer1 = getOrderCustomerSample1();
        OrderCustomer orderCustomer2 = new OrderCustomer();
        assertThat(orderCustomer1).isNotEqualTo(orderCustomer2);

        orderCustomer2.setId(orderCustomer1.getId());
        assertThat(orderCustomer1).isEqualTo(orderCustomer2);

        orderCustomer2 = getOrderCustomerSample2();
        assertThat(orderCustomer1).isNotEqualTo(orderCustomer2);
    }

    @Test
    void ordersTest() {
        OrderCustomer orderCustomer = getOrderCustomerRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderCustomer.addOrders(orderBack);
        assertThat(orderCustomer.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getCustomer()).isEqualTo(orderCustomer);

        orderCustomer.removeOrders(orderBack);
        assertThat(orderCustomer.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getCustomer()).isNull();

        orderCustomer.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(orderCustomer.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getCustomer()).isEqualTo(orderCustomer);

        orderCustomer.setOrders(new HashSet<>());
        assertThat(orderCustomer.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getCustomer()).isNull();
    }
}
