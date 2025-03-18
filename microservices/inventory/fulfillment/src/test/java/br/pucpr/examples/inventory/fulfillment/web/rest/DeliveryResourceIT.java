package br.pucpr.examples.inventory.fulfillment.web.rest;

import static br.pucpr.examples.inventory.fulfillment.domain.DeliveryAsserts.*;
import static br.pucpr.examples.inventory.fulfillment.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.pucpr.examples.inventory.fulfillment.IntegrationTest;
import br.pucpr.examples.inventory.fulfillment.domain.Delivery;
import br.pucpr.examples.inventory.fulfillment.domain.enumeration.DeliveryStatus;
import br.pucpr.examples.inventory.fulfillment.repository.DeliveryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DeliveryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeliveryResourceIT {

    private static final String DEFAULT_ORDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_ID = "BBBBBBBBBB";

    private static final DeliveryStatus DEFAULT_STATUS = DeliveryStatus.CREATED;
    private static final DeliveryStatus UPDATED_STATUS = DeliveryStatus.PREPARING;

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_INSTRUCTIONS = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_INSTRUCTIONS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/deliveries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliveryMockMvc;

    private Delivery delivery;

    private Delivery insertedDelivery;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Delivery createEntity() {
        return new Delivery()
            .orderId(DEFAULT_ORDER_ID)
            .status(DEFAULT_STATUS)
            .customerId(DEFAULT_CUSTOMER_ID)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .zipCode(DEFAULT_ZIP_CODE)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .deliveryInstructions(DEFAULT_DELIVERY_INSTRUCTIONS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Delivery createUpdatedEntity() {
        return new Delivery()
            .orderId(UPDATED_ORDER_ID)
            .status(UPDATED_STATUS)
            .customerId(UPDATED_CUSTOMER_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .zipCode(UPDATED_ZIP_CODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .deliveryInstructions(UPDATED_DELIVERY_INSTRUCTIONS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        delivery = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDelivery != null) {
            deliveryRepository.delete(insertedDelivery);
            insertedDelivery = null;
        }
    }

    @Test
    @Transactional
    void createDelivery() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Delivery
        var returnedDelivery = om.readValue(
            restDeliveryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Delivery.class
        );

        // Validate the Delivery in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDeliveryUpdatableFieldsEquals(returnedDelivery, getPersistedDelivery(returnedDelivery));

        insertedDelivery = returnedDelivery;
    }

    @Test
    @Transactional
    void createDeliveryWithExistingId() throws Exception {
        // Create the Delivery with an existing ID
        delivery.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery)))
            .andExpect(status().isBadRequest());

        // Validate the Delivery in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        delivery.setOrderId(null);

        // Create the Delivery, which fails.

        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        delivery.setStatus(null);

        // Create the Delivery, which fails.

        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCustomerIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        delivery.setCustomerId(null);

        // Create the Delivery, which fails.

        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        delivery.setName(null);

        // Create the Delivery, which fails.

        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        delivery.setEmail(null);

        // Create the Delivery, which fails.

        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressLine1IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        delivery.setAddressLine1(null);

        // Create the Delivery, which fails.

        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressLine2IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        delivery.setAddressLine2(null);

        // Create the Delivery, which fails.

        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkZipCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        delivery.setZipCode(null);

        // Create the Delivery, which fails.

        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        delivery.setCreatedAt(null);

        // Create the Delivery, which fails.

        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        delivery.setUpdatedAt(null);

        // Create the Delivery, which fails.

        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeliveries() throws Exception {
        // Initialize the database
        insertedDelivery = deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList
        restDeliveryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(delivery.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].deliveryInstructions").value(hasItem(DEFAULT_DELIVERY_INSTRUCTIONS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getDelivery() throws Exception {
        // Initialize the database
        insertedDelivery = deliveryRepository.saveAndFlush(delivery);

        // Get the delivery
        restDeliveryMockMvc
            .perform(get(ENTITY_API_URL_ID, delivery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(delivery.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.deliveryInstructions").value(DEFAULT_DELIVERY_INSTRUCTIONS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDelivery() throws Exception {
        // Get the delivery
        restDeliveryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDelivery() throws Exception {
        // Initialize the database
        insertedDelivery = deliveryRepository.saveAndFlush(delivery);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the delivery
        Delivery updatedDelivery = deliveryRepository.findById(delivery.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDelivery are not directly saved in db
        em.detach(updatedDelivery);
        updatedDelivery
            .orderId(UPDATED_ORDER_ID)
            .status(UPDATED_STATUS)
            .customerId(UPDATED_CUSTOMER_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .zipCode(UPDATED_ZIP_CODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .deliveryInstructions(UPDATED_DELIVERY_INSTRUCTIONS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restDeliveryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDelivery.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDelivery))
            )
            .andExpect(status().isOk());

        // Validate the Delivery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDeliveryToMatchAllProperties(updatedDelivery);
    }

    @Test
    @Transactional
    void putNonExistingDelivery() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        delivery.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, delivery.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery))
            )
            .andExpect(status().isBadRequest());

        // Validate the Delivery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDelivery() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        delivery.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(delivery))
            )
            .andExpect(status().isBadRequest());

        // Validate the Delivery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDelivery() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        delivery.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delivery)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Delivery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeliveryWithPatch() throws Exception {
        // Initialize the database
        insertedDelivery = deliveryRepository.saveAndFlush(delivery);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the delivery using partial update
        Delivery partialUpdatedDelivery = new Delivery();
        partialUpdatedDelivery.setId(delivery.getId());

        partialUpdatedDelivery
            .orderId(UPDATED_ORDER_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .deliveryInstructions(UPDATED_DELIVERY_INSTRUCTIONS)
            .createdAt(UPDATED_CREATED_AT);

        restDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDelivery.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDelivery))
            )
            .andExpect(status().isOk());

        // Validate the Delivery in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeliveryUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDelivery, delivery), getPersistedDelivery(delivery));
    }

    @Test
    @Transactional
    void fullUpdateDeliveryWithPatch() throws Exception {
        // Initialize the database
        insertedDelivery = deliveryRepository.saveAndFlush(delivery);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the delivery using partial update
        Delivery partialUpdatedDelivery = new Delivery();
        partialUpdatedDelivery.setId(delivery.getId());

        partialUpdatedDelivery
            .orderId(UPDATED_ORDER_ID)
            .status(UPDATED_STATUS)
            .customerId(UPDATED_CUSTOMER_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .zipCode(UPDATED_ZIP_CODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .deliveryInstructions(UPDATED_DELIVERY_INSTRUCTIONS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDelivery.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDelivery))
            )
            .andExpect(status().isOk());

        // Validate the Delivery in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeliveryUpdatableFieldsEquals(partialUpdatedDelivery, getPersistedDelivery(partialUpdatedDelivery));
    }

    @Test
    @Transactional
    void patchNonExistingDelivery() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        delivery.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, delivery.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(delivery))
            )
            .andExpect(status().isBadRequest());

        // Validate the Delivery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDelivery() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        delivery.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(delivery))
            )
            .andExpect(status().isBadRequest());

        // Validate the Delivery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDelivery() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        delivery.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(delivery)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Delivery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDelivery() throws Exception {
        // Initialize the database
        insertedDelivery = deliveryRepository.saveAndFlush(delivery);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the delivery
        restDeliveryMockMvc
            .perform(delete(ENTITY_API_URL_ID, delivery.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return deliveryRepository.count();
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

    protected Delivery getPersistedDelivery(Delivery delivery) {
        return deliveryRepository.findById(delivery.getId()).orElseThrow();
    }

    protected void assertPersistedDeliveryToMatchAllProperties(Delivery expectedDelivery) {
        assertDeliveryAllPropertiesEquals(expectedDelivery, getPersistedDelivery(expectedDelivery));
    }

    protected void assertPersistedDeliveryToMatchUpdatableProperties(Delivery expectedDelivery) {
        assertDeliveryAllUpdatablePropertiesEquals(expectedDelivery, getPersistedDelivery(expectedDelivery));
    }
}
