package br.pucpr.examples.inventory.fulfillment.repository;

import br.pucpr.examples.inventory.fulfillment.domain.Delivery;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Delivery entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {}
