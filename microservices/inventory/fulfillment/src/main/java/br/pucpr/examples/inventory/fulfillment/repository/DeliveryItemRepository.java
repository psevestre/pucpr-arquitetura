package br.pucpr.examples.inventory.fulfillment.repository;

import br.pucpr.examples.inventory.fulfillment.domain.DeliveryItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DeliveryItem entity.
 */
@Repository
public interface DeliveryItemRepository extends JpaRepository<DeliveryItem, Long> {
    default Optional<DeliveryItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DeliveryItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DeliveryItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select deliveryItem from DeliveryItem deliveryItem left join fetch deliveryItem.delivery",
        countQuery = "select count(deliveryItem) from DeliveryItem deliveryItem"
    )
    Page<DeliveryItem> findAllWithToOneRelationships(Pageable pageable);

    @Query("select deliveryItem from DeliveryItem deliveryItem left join fetch deliveryItem.delivery")
    List<DeliveryItem> findAllWithToOneRelationships();

    @Query("select deliveryItem from DeliveryItem deliveryItem left join fetch deliveryItem.delivery where deliveryItem.id =:id")
    Optional<DeliveryItem> findOneWithToOneRelationships(@Param("id") Long id);
}
