package br.pucpr.examples.inventory.orders.domain;

import static br.pucpr.examples.inventory.orders.domain.OrderCustomerTestSamples.*;
import static br.pucpr.examples.inventory.orders.domain.OrderItemTestSamples.*;
import static br.pucpr.examples.inventory.orders.domain.OrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.pucpr.examples.inventory.orders.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void itemsTest() {
        Order order = getOrderRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        order.addItems(orderItemBack);
        assertThat(order.getItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getOrder()).isEqualTo(order);

        order.removeItems(orderItemBack);
        assertThat(order.getItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getOrder()).isNull();

        order.items(new HashSet<>(Set.of(orderItemBack)));
        assertThat(order.getItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getOrder()).isEqualTo(order);

        order.setItems(new HashSet<>());
        assertThat(order.getItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getOrder()).isNull();
    }

    @Test
    void customerTest() {
        Order order = getOrderRandomSampleGenerator();
        OrderCustomer orderCustomerBack = getOrderCustomerRandomSampleGenerator();

        order.setCustomer(orderCustomerBack);
        assertThat(order.getCustomer()).isEqualTo(orderCustomerBack);

        order.customer(null);
        assertThat(order.getCustomer()).isNull();
    }
}
