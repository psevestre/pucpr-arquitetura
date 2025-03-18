package br.pucpr.examples.inventory.fulfillment.web.rest;

import static br.pucpr.examples.inventory.fulfillment.domain.DeliveryItemAsserts.*;
import static br.pucpr.examples.inventory.fulfillment.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.pucpr.examples.inventory.fulfillment.IntegrationTest;
import br.pucpr.examples.inventory.fulfillment.domain.DeliveryItem;
import br.pucpr.examples.inventory.fulfillment.repository.DeliveryItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DeliveryItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DeliveryItemResourceIT {

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    private static final Double DEFAULT_WEIGHT = 0D;
    private static final Double UPDATED_WEIGHT = 1D;

    private static final Double DEFAULT_VOLUME = 0D;
    private static final Double UPDATED_VOLUME = 1D;

    private static final String ENTITY_API_URL = "/api/delivery-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DeliveryItemRepository deliveryItemRepository;

    @Mock
    private DeliveryItemRepository deliveryItemRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliveryItemMockMvc;

    private DeliveryItem deliveryItem;

    private DeliveryItem insertedDeliveryItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryItem createEntity() {
        return new DeliveryItem()
            .sku(DEFAULT_SKU)
            .description(DEFAULT_DESCRIPTION)
            .quantity(DEFAULT_QUANTITY)
            .weight(DEFAULT_WEIGHT)
            .volume(DEFAULT_VOLUME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryItem createUpdatedEntity() {
        return new DeliveryItem()
            .sku(UPDATED_SKU)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .weight(UPDATED_WEIGHT)
            .volume(UPDATED_VOLUME);
    }

    @BeforeEach
    public void initTest() {
        deliveryItem = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDeliveryItem != null) {
            deliveryItemRepository.delete(insertedDeliveryItem);
            insertedDeliveryItem = null;
        }
    }

    @Test
    @Transactional
    void createDeliveryItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DeliveryItem
        var returnedDeliveryItem = om.readValue(
            restDeliveryItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deliveryItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DeliveryItem.class
        );

        // Validate the DeliveryItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDeliveryItemUpdatableFieldsEquals(returnedDeliveryItem, getPersistedDeliveryItem(returnedDeliveryItem));

        insertedDeliveryItem = returnedDeliveryItem;
    }

    @Test
    @Transactional
    void createDeliveryItemWithExistingId() throws Exception {
        // Create the DeliveryItem with an existing ID
        deliveryItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deliveryItem)))
            .andExpect(status().isBadRequest());

        // Validate the DeliveryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSkuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deliveryItem.setSku(null);

        // Create the DeliveryItem, which fails.

        restDeliveryItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deliveryItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deliveryItem.setQuantity(null);

        // Create the DeliveryItem, which fails.

        restDeliveryItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deliveryItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeightIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deliveryItem.setWeight(null);

        // Create the DeliveryItem, which fails.

        restDeliveryItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deliveryItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVolumeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deliveryItem.setVolume(null);

        // Create the DeliveryItem, which fails.

        restDeliveryItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deliveryItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeliveryItems() throws Exception {
        // Initialize the database
        insertedDeliveryItem = deliveryItemRepository.saveAndFlush(deliveryItem);

        // Get all the deliveryItemList
        restDeliveryItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeliveryItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(deliveryItemRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeliveryItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(deliveryItemRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeliveryItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(deliveryItemRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeliveryItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(deliveryItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDeliveryItem() throws Exception {
        // Initialize the database
        insertedDeliveryItem = deliveryItemRepository.saveAndFlush(deliveryItem);

        // Get the deliveryItem
        restDeliveryItemMockMvc
            .perform(get(ENTITY_API_URL_ID, deliveryItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryItem.getId().intValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME));
    }

    @Test
    @Transactional
    void getNonExistingDeliveryItem() throws Exception {
        // Get the deliveryItem
        restDeliveryItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeliveryItem() throws Exception {
        // Initialize the database
        insertedDeliveryItem = deliveryItemRepository.saveAndFlush(deliveryItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deliveryItem
        DeliveryItem updatedDeliveryItem = deliveryItemRepository.findById(deliveryItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeliveryItem are not directly saved in db
        em.detach(updatedDeliveryItem);
        updatedDeliveryItem
            .sku(UPDATED_SKU)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .weight(UPDATED_WEIGHT)
            .volume(UPDATED_VOLUME);

        restDeliveryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeliveryItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDeliveryItem))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDeliveryItemToMatchAllProperties(updatedDeliveryItem);
    }

    @Test
    @Transactional
    void putNonExistingDeliveryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deliveryItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deliveryItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeliveryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deliveryItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deliveryItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeliveryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deliveryItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deliveryItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeliveryItemWithPatch() throws Exception {
        // Initialize the database
        insertedDeliveryItem = deliveryItemRepository.saveAndFlush(deliveryItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deliveryItem using partial update
        DeliveryItem partialUpdatedDeliveryItem = new DeliveryItem();
        partialUpdatedDeliveryItem.setId(deliveryItem.getId());

        partialUpdatedDeliveryItem.sku(UPDATED_SKU).description(UPDATED_DESCRIPTION).quantity(UPDATED_QUANTITY).volume(UPDATED_VOLUME);

        restDeliveryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeliveryItem))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeliveryItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDeliveryItem, deliveryItem),
            getPersistedDeliveryItem(deliveryItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateDeliveryItemWithPatch() throws Exception {
        // Initialize the database
        insertedDeliveryItem = deliveryItemRepository.saveAndFlush(deliveryItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deliveryItem using partial update
        DeliveryItem partialUpdatedDeliveryItem = new DeliveryItem();
        partialUpdatedDeliveryItem.setId(deliveryItem.getId());

        partialUpdatedDeliveryItem
            .sku(UPDATED_SKU)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .weight(UPDATED_WEIGHT)
            .volume(UPDATED_VOLUME);

        restDeliveryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeliveryItem))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeliveryItemUpdatableFieldsEquals(partialUpdatedDeliveryItem, getPersistedDeliveryItem(partialUpdatedDeliveryItem));
    }

    @Test
    @Transactional
    void patchNonExistingDeliveryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deliveryItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deliveryItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deliveryItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeliveryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deliveryItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deliveryItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeliveryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deliveryItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(deliveryItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeliveryItem() throws Exception {
        // Initialize the database
        insertedDeliveryItem = deliveryItemRepository.saveAndFlush(deliveryItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the deliveryItem
        restDeliveryItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, deliveryItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return deliveryItemRepository.count();
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

    protected DeliveryItem getPersistedDeliveryItem(DeliveryItem deliveryItem) {
        return deliveryItemRepository.findById(deliveryItem.getId()).orElseThrow();
    }

    protected void assertPersistedDeliveryItemToMatchAllProperties(DeliveryItem expectedDeliveryItem) {
        assertDeliveryItemAllPropertiesEquals(expectedDeliveryItem, getPersistedDeliveryItem(expectedDeliveryItem));
    }

    protected void assertPersistedDeliveryItemToMatchUpdatableProperties(DeliveryItem expectedDeliveryItem) {
        assertDeliveryItemAllUpdatablePropertiesEquals(expectedDeliveryItem, getPersistedDeliveryItem(expectedDeliveryItem));
    }
}
