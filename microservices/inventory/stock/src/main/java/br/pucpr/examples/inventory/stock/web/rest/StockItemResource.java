package br.pucpr.examples.inventory.stock.web.rest;

import br.pucpr.examples.inventory.stock.domain.StockItem;
import br.pucpr.examples.inventory.stock.repository.StockItemRepository;
import br.pucpr.examples.inventory.stock.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link br.pucpr.examples.inventory.stock.domain.StockItem}.
 */
@RestController
@RequestMapping("/api/stock-items")
@Transactional
public class StockItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(StockItemResource.class);

    private static final String ENTITY_NAME = "stockStockItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockItemRepository stockItemRepository;

    public StockItemResource(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    /**
     * {@code POST  /stock-items} : Create a new stockItem.
     *
     * @param stockItem the stockItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockItem, or with status {@code 400 (Bad Request)} if the stockItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StockItem> createStockItem(@Valid @RequestBody StockItem stockItem) throws URISyntaxException {
        LOG.debug("REST request to save StockItem : {}", stockItem);
        if (stockItem.getId() != null) {
            throw new BadRequestAlertException("A new stockItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stockItem = stockItemRepository.save(stockItem);
        return ResponseEntity.created(new URI("/api/stock-items/" + stockItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, stockItem.getId().toString()))
            .body(stockItem);
    }

    /**
     * {@code PUT  /stock-items/:id} : Updates an existing stockItem.
     *
     * @param id the id of the stockItem to save.
     * @param stockItem the stockItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockItem,
     * or with status {@code 400 (Bad Request)} if the stockItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StockItem> updateStockItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StockItem stockItem
    ) throws URISyntaxException {
        LOG.debug("REST request to update StockItem : {}, {}", id, stockItem);
        if (stockItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        stockItem = stockItemRepository.save(stockItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockItem.getId().toString()))
            .body(stockItem);
    }

    /**
     * {@code PATCH  /stock-items/:id} : Partial updates given fields of an existing stockItem, field will ignore if it is null
     *
     * @param id the id of the stockItem to save.
     * @param stockItem the stockItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockItem,
     * or with status {@code 400 (Bad Request)} if the stockItem is not valid,
     * or with status {@code 404 (Not Found)} if the stockItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StockItem> partialUpdateStockItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StockItem stockItem
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StockItem partially : {}, {}", id, stockItem);
        if (stockItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StockItem> result = stockItemRepository
            .findById(stockItem.getId())
            .map(existingStockItem -> {
                if (stockItem.getSku() != null) {
                    existingStockItem.setSku(stockItem.getSku());
                }
                if (stockItem.getDescription() != null) {
                    existingStockItem.setDescription(stockItem.getDescription());
                }
                if (stockItem.getAvailable() != null) {
                    existingStockItem.setAvailable(stockItem.getAvailable());
                }
                if (stockItem.getReserved() != null) {
                    existingStockItem.setReserved(stockItem.getReserved());
                }
                if (stockItem.getMinStock() != null) {
                    existingStockItem.setMinStock(stockItem.getMinStock());
                }

                return existingStockItem;
            })
            .map(stockItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockItem.getId().toString())
        );
    }

    /**
     * {@code GET  /stock-items} : get all the stockItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stockItems in body.
     */
    @GetMapping("")
    public List<StockItem> getAllStockItems() {
        LOG.debug("REST request to get all StockItems");
        return stockItemRepository.findAll();
    }

    /**
     * {@code GET  /stock-items/:id} : get the "id" stockItem.
     *
     * @param id the id of the stockItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StockItem> getStockItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StockItem : {}", id);
        Optional<StockItem> stockItem = stockItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(stockItem);
    }

    /**
     * {@code DELETE  /stock-items/:id} : delete the "id" stockItem.
     *
     * @param id the id of the stockItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StockItem : {}", id);
        stockItemRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
