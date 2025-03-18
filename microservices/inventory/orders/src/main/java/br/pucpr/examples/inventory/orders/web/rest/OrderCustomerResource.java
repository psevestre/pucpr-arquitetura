package br.pucpr.examples.inventory.orders.web.rest;

import br.pucpr.examples.inventory.orders.domain.OrderCustomer;
import br.pucpr.examples.inventory.orders.repository.OrderCustomerRepository;
import br.pucpr.examples.inventory.orders.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link br.pucpr.examples.inventory.orders.domain.OrderCustomer}.
 */
@RestController
@RequestMapping("/api/order-customers")
@Transactional
public class OrderCustomerResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderCustomerResource.class);

    private static final String ENTITY_NAME = "ordersOrderCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderCustomerRepository orderCustomerRepository;

    public OrderCustomerResource(OrderCustomerRepository orderCustomerRepository) {
        this.orderCustomerRepository = orderCustomerRepository;
    }

    /**
     * {@code POST  /order-customers} : Create a new orderCustomer.
     *
     * @param orderCustomer the orderCustomer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderCustomer, or with status {@code 400 (Bad Request)} if the orderCustomer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderCustomer> createOrderCustomer(@Valid @RequestBody OrderCustomer orderCustomer) throws URISyntaxException {
        LOG.debug("REST request to save OrderCustomer : {}", orderCustomer);
        if (orderCustomer.getId() != null) {
            throw new BadRequestAlertException("A new orderCustomer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderCustomer = orderCustomerRepository.save(orderCustomer);
        return ResponseEntity.created(new URI("/api/order-customers/" + orderCustomer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderCustomer.getId().toString()))
            .body(orderCustomer);
    }

    /**
     * {@code PUT  /order-customers/:id} : Updates an existing orderCustomer.
     *
     * @param id the id of the orderCustomer to save.
     * @param orderCustomer the orderCustomer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderCustomer,
     * or with status {@code 400 (Bad Request)} if the orderCustomer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderCustomer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderCustomer> updateOrderCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderCustomer orderCustomer
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderCustomer : {}, {}", id, orderCustomer);
        if (orderCustomer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderCustomer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderCustomerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderCustomer = orderCustomerRepository.save(orderCustomer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderCustomer.getId().toString()))
            .body(orderCustomer);
    }

    /**
     * {@code PATCH  /order-customers/:id} : Partial updates given fields of an existing orderCustomer, field will ignore if it is null
     *
     * @param id the id of the orderCustomer to save.
     * @param orderCustomer the orderCustomer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderCustomer,
     * or with status {@code 400 (Bad Request)} if the orderCustomer is not valid,
     * or with status {@code 404 (Not Found)} if the orderCustomer is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderCustomer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderCustomer> partialUpdateOrderCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderCustomer orderCustomer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderCustomer partially : {}, {}", id, orderCustomer);
        if (orderCustomer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderCustomer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderCustomerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderCustomer> result = orderCustomerRepository
            .findById(orderCustomer.getId())
            .map(existingOrderCustomer -> {
                if (orderCustomer.getCustomerId() != null) {
                    existingOrderCustomer.setCustomerId(orderCustomer.getCustomerId());
                }
                if (orderCustomer.getName() != null) {
                    existingOrderCustomer.setName(orderCustomer.getName());
                }
                if (orderCustomer.getEmail() != null) {
                    existingOrderCustomer.setEmail(orderCustomer.getEmail());
                }
                if (orderCustomer.getAddressLine1() != null) {
                    existingOrderCustomer.setAddressLine1(orderCustomer.getAddressLine1());
                }
                if (orderCustomer.getAddressLine2() != null) {
                    existingOrderCustomer.setAddressLine2(orderCustomer.getAddressLine2());
                }
                if (orderCustomer.getZipCode() != null) {
                    existingOrderCustomer.setZipCode(orderCustomer.getZipCode());
                }
                if (orderCustomer.getCity() != null) {
                    existingOrderCustomer.setCity(orderCustomer.getCity());
                }
                if (orderCustomer.getCountry() != null) {
                    existingOrderCustomer.setCountry(orderCustomer.getCountry());
                }

                return existingOrderCustomer;
            })
            .map(orderCustomerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderCustomer.getId().toString())
        );
    }

    /**
     * {@code GET  /order-customers} : get all the orderCustomers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderCustomers in body.
     */
    @GetMapping("")
    public List<OrderCustomer> getAllOrderCustomers() {
        LOG.debug("REST request to get all OrderCustomers");
        return orderCustomerRepository.findAll();
    }

    /**
     * {@code GET  /order-customers/:id} : get the "id" orderCustomer.
     *
     * @param id the id of the orderCustomer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderCustomer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderCustomer> getOrderCustomer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderCustomer : {}", id);
        Optional<OrderCustomer> orderCustomer = orderCustomerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orderCustomer);
    }

    /**
     * {@code DELETE  /order-customers/:id} : delete the "id" orderCustomer.
     *
     * @param id the id of the orderCustomer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderCustomer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderCustomer : {}", id);
        orderCustomerRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
