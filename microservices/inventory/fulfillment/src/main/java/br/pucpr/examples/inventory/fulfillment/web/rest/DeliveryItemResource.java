package br.pucpr.examples.inventory.fulfillment.web.rest;

import br.pucpr.examples.inventory.fulfillment.domain.DeliveryItem;
import br.pucpr.examples.inventory.fulfillment.repository.DeliveryItemRepository;
import br.pucpr.examples.inventory.fulfillment.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.pucpr.examples.inventory.fulfillment.domain.DeliveryItem}.
 */
@RestController
@RequestMapping("/api/delivery-items")
@Transactional
public class DeliveryItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(DeliveryItemResource.class);

    private static final String ENTITY_NAME = "fulfillmentDeliveryItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryItemRepository deliveryItemRepository;

    public DeliveryItemResource(DeliveryItemRepository deliveryItemRepository) {
        this.deliveryItemRepository = deliveryItemRepository;
    }

    /**
     * {@code POST  /delivery-items} : Create a new deliveryItem.
     *
     * @param deliveryItem the deliveryItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliveryItem, or with status {@code 400 (Bad Request)} if the deliveryItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DeliveryItem> createDeliveryItem(@Valid @RequestBody DeliveryItem deliveryItem) throws URISyntaxException {
        LOG.debug("REST request to save DeliveryItem : {}", deliveryItem);
        if (deliveryItem.getId() != null) {
            throw new BadRequestAlertException("A new deliveryItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        deliveryItem = deliveryItemRepository.save(deliveryItem);
        return ResponseEntity.created(new URI("/api/delivery-items/" + deliveryItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, deliveryItem.getId().toString()))
            .body(deliveryItem);
    }

    /**
     * {@code PUT  /delivery-items/:id} : Updates an existing deliveryItem.
     *
     * @param id the id of the deliveryItem to save.
     * @param deliveryItem the deliveryItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryItem,
     * or with status {@code 400 (Bad Request)} if the deliveryItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliveryItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DeliveryItem> updateDeliveryItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeliveryItem deliveryItem
    ) throws URISyntaxException {
        LOG.debug("REST request to update DeliveryItem : {}, {}", id, deliveryItem);
        if (deliveryItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        deliveryItem = deliveryItemRepository.save(deliveryItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryItem.getId().toString()))
            .body(deliveryItem);
    }

    /**
     * {@code PATCH  /delivery-items/:id} : Partial updates given fields of an existing deliveryItem, field will ignore if it is null
     *
     * @param id the id of the deliveryItem to save.
     * @param deliveryItem the deliveryItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryItem,
     * or with status {@code 400 (Bad Request)} if the deliveryItem is not valid,
     * or with status {@code 404 (Not Found)} if the deliveryItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliveryItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeliveryItem> partialUpdateDeliveryItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeliveryItem deliveryItem
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DeliveryItem partially : {}, {}", id, deliveryItem);
        if (deliveryItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeliveryItem> result = deliveryItemRepository
            .findById(deliveryItem.getId())
            .map(existingDeliveryItem -> {
                if (deliveryItem.getSku() != null) {
                    existingDeliveryItem.setSku(deliveryItem.getSku());
                }
                if (deliveryItem.getDescription() != null) {
                    existingDeliveryItem.setDescription(deliveryItem.getDescription());
                }
                if (deliveryItem.getQuantity() != null) {
                    existingDeliveryItem.setQuantity(deliveryItem.getQuantity());
                }
                if (deliveryItem.getWeight() != null) {
                    existingDeliveryItem.setWeight(deliveryItem.getWeight());
                }
                if (deliveryItem.getVolume() != null) {
                    existingDeliveryItem.setVolume(deliveryItem.getVolume());
                }

                return existingDeliveryItem;
            })
            .map(deliveryItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryItem.getId().toString())
        );
    }

    /**
     * {@code GET  /delivery-items} : get all the deliveryItems.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryItems in body.
     */
    @GetMapping("")
    public List<DeliveryItem> getAllDeliveryItems(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all DeliveryItems");
        if (eagerload) {
            return deliveryItemRepository.findAllWithEagerRelationships();
        } else {
            return deliveryItemRepository.findAll();
        }
    }

    /**
     * {@code GET  /delivery-items/:id} : get the "id" deliveryItem.
     *
     * @param id the id of the deliveryItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryItem> getDeliveryItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DeliveryItem : {}", id);
        Optional<DeliveryItem> deliveryItem = deliveryItemRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(deliveryItem);
    }

    /**
     * {@code DELETE  /delivery-items/:id} : delete the "id" deliveryItem.
     *
     * @param id the id of the deliveryItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeliveryItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DeliveryItem : {}", id);
        deliveryItemRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
