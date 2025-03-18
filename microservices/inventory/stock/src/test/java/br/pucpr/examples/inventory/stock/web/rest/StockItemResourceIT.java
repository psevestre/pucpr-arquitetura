package br.pucpr.examples.inventory.stock.web.rest;

import static br.pucpr.examples.inventory.stock.domain.StockItemAsserts.*;
import static br.pucpr.examples.inventory.stock.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.pucpr.examples.inventory.stock.IntegrationTest;
import br.pucpr.examples.inventory.stock.domain.StockItem;
import br.pucpr.examples.inventory.stock.repository.StockItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StockItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockItemResourceIT {

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_AVAILABLE = 1;
    private static final Integer UPDATED_AVAILABLE = 2;

    private static final Integer DEFAULT_RESERVED = 0;
    private static final Integer UPDATED_RESERVED = 1;

    private static final Integer DEFAULT_MIN_STOCK = 0;
    private static final Integer UPDATED_MIN_STOCK = 1;

    private static final String ENTITY_API_URL = "/api/stock-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StockItemRepository stockItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockItemMockMvc;

    private StockItem stockItem;

    private StockItem insertedStockItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockItem createEntity() {
        return new StockItem()
            .sku(DEFAULT_SKU)
            .description(DEFAULT_DESCRIPTION)
            .available(DEFAULT_AVAILABLE)
            .reserved(DEFAULT_RESERVED)
            .minStock(DEFAULT_MIN_STOCK);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockItem createUpdatedEntity() {
        return new StockItem()
            .sku(UPDATED_SKU)
            .description(UPDATED_DESCRIPTION)
            .available(UPDATED_AVAILABLE)
            .reserved(UPDATED_RESERVED)
            .minStock(UPDATED_MIN_STOCK);
    }

    @BeforeEach
    public void initTest() {
        stockItem = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStockItem != null) {
            stockItemRepository.delete(insertedStockItem);
            insertedStockItem = null;
        }
    }

    @Test
    @Transactional
    void createStockItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StockItem
        var returnedStockItem = om.readValue(
            restStockItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StockItem.class
        );

        // Validate the StockItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertStockItemUpdatableFieldsEquals(returnedStockItem, getPersistedStockItem(returnedStockItem));

        insertedStockItem = returnedStockItem;
    }

    @Test
    @Transactional
    void createStockItemWithExistingId() throws Exception {
        // Create the StockItem with an existing ID
        stockItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockItem)))
            .andExpect(status().isBadRequest());

        // Validate the StockItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSkuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockItem.setSku(null);

        // Create the StockItem, which fails.

        restStockItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockItem.setDescription(null);

        // Create the StockItem, which fails.

        restStockItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockItem.setAvailable(null);

        // Create the StockItem, which fails.

        restStockItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReservedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockItem.setReserved(null);

        // Create the StockItem, which fails.

        restStockItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockItem.setMinStock(null);

        // Create the StockItem, which fails.

        restStockItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStockItems() throws Exception {
        // Initialize the database
        insertedStockItem = stockItemRepository.saveAndFlush(stockItem);

        // Get all the stockItemList
        restStockItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE)))
            .andExpect(jsonPath("$.[*].reserved").value(hasItem(DEFAULT_RESERVED)))
            .andExpect(jsonPath("$.[*].minStock").value(hasItem(DEFAULT_MIN_STOCK)));
    }

    @Test
    @Transactional
    void getStockItem() throws Exception {
        // Initialize the database
        insertedStockItem = stockItemRepository.saveAndFlush(stockItem);

        // Get the stockItem
        restStockItemMockMvc
            .perform(get(ENTITY_API_URL_ID, stockItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockItem.getId().intValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE))
            .andExpect(jsonPath("$.reserved").value(DEFAULT_RESERVED))
            .andExpect(jsonPath("$.minStock").value(DEFAULT_MIN_STOCK));
    }

    @Test
    @Transactional
    void getNonExistingStockItem() throws Exception {
        // Get the stockItem
        restStockItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStockItem() throws Exception {
        // Initialize the database
        insertedStockItem = stockItemRepository.saveAndFlush(stockItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockItem
        StockItem updatedStockItem = stockItemRepository.findById(stockItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStockItem are not directly saved in db
        em.detach(updatedStockItem);
        updatedStockItem
            .sku(UPDATED_SKU)
            .description(UPDATED_DESCRIPTION)
            .available(UPDATED_AVAILABLE)
            .reserved(UPDATED_RESERVED)
            .minStock(UPDATED_MIN_STOCK);

        restStockItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStockItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedStockItem))
            )
            .andExpect(status().isOk());

        // Validate the StockItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStockItemToMatchAllProperties(updatedStockItem);
    }

    @Test
    @Transactional
    void putNonExistingStockItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockItem.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockItemWithPatch() throws Exception {
        // Initialize the database
        insertedStockItem = stockItemRepository.saveAndFlush(stockItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockItem using partial update
        StockItem partialUpdatedStockItem = new StockItem();
        partialUpdatedStockItem.setId(stockItem.getId());

        partialUpdatedStockItem
            .description(UPDATED_DESCRIPTION)
            .available(UPDATED_AVAILABLE)
            .reserved(UPDATED_RESERVED)
            .minStock(UPDATED_MIN_STOCK);

        restStockItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStockItem))
            )
            .andExpect(status().isOk());

        // Validate the StockItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStockItem, stockItem),
            getPersistedStockItem(stockItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateStockItemWithPatch() throws Exception {
        // Initialize the database
        insertedStockItem = stockItemRepository.saveAndFlush(stockItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockItem using partial update
        StockItem partialUpdatedStockItem = new StockItem();
        partialUpdatedStockItem.setId(stockItem.getId());

        partialUpdatedStockItem
            .sku(UPDATED_SKU)
            .description(UPDATED_DESCRIPTION)
            .available(UPDATED_AVAILABLE)
            .reserved(UPDATED_RESERVED)
            .minStock(UPDATED_MIN_STOCK);

        restStockItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStockItem))
            )
            .andExpect(status().isOk());

        // Validate the StockItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockItemUpdatableFieldsEquals(partialUpdatedStockItem, getPersistedStockItem(partialUpdatedStockItem));
    }

    @Test
    @Transactional
    void patchNonExistingStockItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stockItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stockItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stockItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockItem() throws Exception {
        // Initialize the database
        insertedStockItem = stockItemRepository.saveAndFlush(stockItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the stockItem
        restStockItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return stockItemRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected StockItem getPersistedStockItem(StockItem stockItem) {
        return stockItemRepository.findById(stockItem.getId()).orElseThrow();
    }

    protected void assertPersistedStockItemToMatchAllProperties(StockItem expectedStockItem) {
        assertStockItemAllPropertiesEquals(expectedStockItem, getPersistedStockItem(expectedStockItem));
    }

    protected void assertPersistedStockItemToMatchUpdatableProperties(StockItem expectedStockItem) {
        assertStockItemAllUpdatablePropertiesEquals(expectedStockItem, getPersistedStockItem(expectedStockItem));
    }
}
