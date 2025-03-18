package br.pucpr.examples.inventory.orders.repository;

import br.pucpr.examples.inventory.orders.domain.OrderCustomer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderCustomer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderCustomerRepository extends JpaRepository<OrderCustomer, Long> {}
