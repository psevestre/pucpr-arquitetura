package br.pucpr.examples.inventory.stock.repository;

import br.pucpr.examples.inventory.stock.domain.StockItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StockItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Long> {}
