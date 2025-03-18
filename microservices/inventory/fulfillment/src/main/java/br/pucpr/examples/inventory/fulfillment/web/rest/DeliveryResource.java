package br.pucpr.examples.inventory.fulfillment.web.rest;

import br.pucpr.examples.inventory.fulfillment.domain.Delivery;
import br.pucpr.examples.inventory.fulfillment.repository.DeliveryRepository;
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
 * REST controller for managing {@link br.pucpr.examples.inventory.fulfillment.domain.Delivery}.
 */
@RestController
@RequestMapping("/api/deliveries")
@Transactional
public class DeliveryResource {

    private static final Logger LOG = LoggerFactory.getLogger(DeliveryResource.class);

    private static final String ENTITY_NAME = "fulfillmentDelivery";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryRepository deliveryRepository;

    public DeliveryResource(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    /**
     * {@code POST  /deliveries} : Create a new delivery.
     *
     * @param delivery the delivery to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new delivery, or with status {@code 400 (Bad Request)} if the delivery has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Delivery> createDelivery(@Valid @RequestBody Delivery delivery) throws URISyntaxException {
        LOG.debug("REST request to save Delivery : {}", delivery);
        if (delivery.getId() != null) {
            throw new BadRequestAlertException("A new delivery cannot already have an ID", ENTITY_NAME, "idexists");
        }
        delivery = deliveryRepository.save(delivery);
        return ResponseEntity.created(new URI("/api/deliveries/" + delivery.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, delivery.getId().toString()))
            .body(delivery);
    }

    /**
     * {@code PUT  /deliveries/:id} : Updates an existing delivery.
     *
     * @param id the id of the delivery to save.
     * @param delivery the delivery to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated delivery,
     * or with status {@code 400 (Bad Request)} if the delivery is not valid,
     * or with status {@code 500 (Internal Server Error)} if the delivery couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Delivery> updateDelivery(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Delivery delivery
    ) throws URISyntaxException {
        LOG.debug("REST request to update Delivery : {}, {}", id, delivery);
        if (delivery.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, delivery.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        delivery = deliveryRepository.save(delivery);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, delivery.getId().toString()))
            .body(delivery);
    }

    /**
     * {@code PATCH  /deliveries/:id} : Partial updates given fields of an existing delivery, field will ignore if it is null
     *
     * @param id the id of the delivery to save.
     * @param delivery the delivery to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated delivery,
     * or with status {@code 400 (Bad Request)} if the delivery is not valid,
     * or with status {@code 404 (Not Found)} if the delivery is not found,
     * or with status {@code 500 (Internal Server Error)} if the delivery couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Delivery> partialUpdateDelivery(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Delivery delivery
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Delivery partially : {}, {}", id, delivery);
        if (delivery.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, delivery.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Delivery> result = deliveryRepository
            .findById(delivery.getId())
            .map(existingDelivery -> {
                if (delivery.getOrderId() != null) {
                    existingDelivery.setOrderId(delivery.getOrderId());
                }
                if (delivery.getStatus() != null) {
                    existingDelivery.setStatus(delivery.getStatus());
                }
                if (delivery.getCustomerId() != null) {
                    existingDelivery.setCustomerId(delivery.getCustomerId());
                }
                if (delivery.getName() != null) {
                    existingDelivery.setName(delivery.getName());
                }
                if (delivery.getEmail() != null) {
                    existingDelivery.setEmail(delivery.getEmail());
                }
                if (delivery.getAddressLine1() != null) {
                    existingDelivery.setAddressLine1(delivery.getAddressLine1());
                }
                if (delivery.getAddressLine2() != null) {
                    existingDelivery.setAddressLine2(delivery.getAddressLine2());
                }
                if (delivery.getZipCode() != null) {
                    existingDelivery.setZipCode(delivery.getZipCode());
                }
                if (delivery.getCity() != null) {
                    existingDelivery.setCity(delivery.getCity());
                }
                if (delivery.getCountry() != null) {
                    existingDelivery.setCountry(delivery.getCountry());
                }
                if (delivery.getDeliveryInstructions() != null) {
                    existingDelivery.setDeliveryInstructions(delivery.getDeliveryInstructions());
                }
                if (delivery.getCreatedAt() != null) {
                    existingDelivery.setCreatedAt(delivery.getCreatedAt());
                }
                if (delivery.getUpdatedAt() != null) {
                    existingDelivery.setUpdatedAt(delivery.getUpdatedAt());
                }

                return existingDelivery;
            })
            .map(deliveryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, delivery.getId().toString())
        );
    }

    /**
     * {@code GET  /deliveries} : get all the deliveries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveries in body.
     */
    @GetMapping("")
    public List<Delivery> getAllDeliveries() {
        LOG.debug("REST request to get all Deliveries");
        return deliveryRepository.findAll();
    }

    /**
     * {@code GET  /deliveries/:id} : get the "id" delivery.
     *
     * @param id the id of the delivery to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the delivery, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getDelivery(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Delivery : {}", id);
        Optional<Delivery> delivery = deliveryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(delivery);
    }

    /**
     * {@code DELETE  /deliveries/:id} : delete the "id" delivery.
     *
     * @param id the id of the delivery to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Delivery : {}", id);
        deliveryRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
