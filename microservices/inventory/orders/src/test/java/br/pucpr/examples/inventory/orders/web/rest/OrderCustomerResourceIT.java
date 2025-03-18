package br.pucpr.examples.inventory.orders.web.rest;

import static br.pucpr.examples.inventory.orders.domain.OrderCustomerAsserts.*;
import static br.pucpr.examples.inventory.orders.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.pucpr.examples.inventory.orders.IntegrationTest;
import br.pucpr.examples.inventory.orders.domain.OrderCustomer;
import br.pucpr.examples.inventory.orders.repository.OrderCustomerRepository;
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
 * Integration tests for the {@link OrderCustomerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderCustomerResourceIT {

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

    private static final String ENTITY_API_URL = "/api/order-customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderCustomerRepository orderCustomerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderCustomerMockMvc;

    private OrderCustomer orderCustomer;

    private OrderCustomer insertedOrderCustomer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderCustomer createEntity() {
        return new OrderCustomer()
            .customerId(DEFAULT_CUSTOMER_ID)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .zipCode(DEFAULT_ZIP_CODE)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderCustomer createUpdatedEntity() {
        return new OrderCustomer()
            .customerId(UPDATED_CUSTOMER_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .zipCode(UPDATED_ZIP_CODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);
    }

    @BeforeEach
    public void initTest() {
        orderCustomer = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrderCustomer != null) {
            orderCustomerRepository.delete(insertedOrderCustomer);
            insertedOrderCustomer = null;
        }
    }

    @Test
    @Transactional
    void createOrderCustomer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderCustomer
        var returnedOrderCustomer = om.readValue(
            restOrderCustomerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderCustomer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderCustomer.class
        );

        // Validate the OrderCustomer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderCustomerUpdatableFieldsEquals(returnedOrderCustomer, getPersistedOrderCustomer(returnedOrderCustomer));

        insertedOrderCustomer = returnedOrderCustomer;
    }

    @Test
    @Transactional
    void createOrderCustomerWithExistingId() throws Exception {
        // Create the OrderCustomer with an existing ID
        orderCustomer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderCustomer)))
            .andExpect(status().isBadRequest());

        // Validate the OrderCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCustomerIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderCustomer.setCustomerId(null);

        // Create the OrderCustomer, which fails.

        restOrderCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderCustomer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderCustomer.setName(null);

        // Create the OrderCustomer, which fails.

        restOrderCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderCustomer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderCustomer.setEmail(null);

        // Create the OrderCustomer, which fails.

        restOrderCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderCustomer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressLine1IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderCustomer.setAddressLine1(null);

        // Create the OrderCustomer, which fails.

        restOrderCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderCustomer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressLine2IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderCustomer.setAddressLine2(null);

        // Create the OrderCustomer, which fails.

        restOrderCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderCustomer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkZipCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderCustomer.setZipCode(null);

        // Create the OrderCustomer, which fails.

        restOrderCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderCustomer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderCustomers() throws Exception {
        // Initialize the database
        insertedOrderCustomer = orderCustomerRepository.saveAndFlush(orderCustomer);

        // Get all the orderCustomerList
        restOrderCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));
    }

    @Test
    @Transactional
    void getOrderCustomer() throws Exception {
        // Initialize the database
        insertedOrderCustomer = orderCustomerRepository.saveAndFlush(orderCustomer);

        // Get the orderCustomer
        restOrderCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, orderCustomer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderCustomer.getId().intValue()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY));
    }

    @Test
    @Transactional
    void getNonExistingOrderCustomer() throws Exception {
        // Get the orderCustomer
        restOrderCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderCustomer() throws Exception {
        // Initialize the database
        insertedOrderCustomer = orderCustomerRepository.saveAndFlush(orderCustomer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderCustomer
        OrderCustomer updatedOrderCustomer = orderCustomerRepository.findById(orderCustomer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderCustomer are not directly saved in db
        em.detach(updatedOrderCustomer);
        updatedOrderCustomer
            .customerId(UPDATED_CUSTOMER_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .zipCode(UPDATED_ZIP_CODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restOrderCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderCustomer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderCustomer))
            )
            .andExpect(status().isOk());

        // Validate the OrderCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderCustomerToMatchAllProperties(updatedOrderCustomer);
    }

    @Test
    @Transactional
    void putNonExistingOrderCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderCustomer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderCustomer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderCustomer))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderCustomer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderCustomer))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderCustomer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderCustomer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderCustomerWithPatch() throws Exception {
        // Initialize the database
        insertedOrderCustomer = orderCustomerRepository.saveAndFlush(orderCustomer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderCustomer using partial update
        OrderCustomer partialUpdatedOrderCustomer = new OrderCustomer();
        partialUpdatedOrderCustomer.setId(orderCustomer.getId());

        partialUpdatedOrderCustomer
            .email(UPDATED_EMAIL)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .zipCode(UPDATED_ZIP_CODE)
            .city(UPDATED_CITY);

        restOrderCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderCustomer))
            )
            .andExpect(status().isOk());

        // Validate the OrderCustomer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderCustomerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderCustomer, orderCustomer),
            getPersistedOrderCustomer(orderCustomer)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderCustomerWithPatch() throws Exception {
        // Initialize the database
        insertedOrderCustomer = orderCustomerRepository.saveAndFlush(orderCustomer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderCustomer using partial update
        OrderCustomer partialUpdatedOrderCustomer = new OrderCustomer();
        partialUpdatedOrderCustomer.setId(orderCustomer.getId());

        partialUpdatedOrderCustomer
            .customerId(UPDATED_CUSTOMER_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .zipCode(UPDATED_ZIP_CODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restOrderCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderCustomer))
            )
            .andExpect(status().isOk());

        // Validate the OrderCustomer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderCustomerUpdatableFieldsEquals(partialUpdatedOrderCustomer, getPersistedOrderCustomer(partialUpdatedOrderCustomer));
    }

    @Test
    @Transactional
    void patchNonExistingOrderCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderCustomer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderCustomer))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderCustomer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderCustomer))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderCustomer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderCustomerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderCustomer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderCustomer() throws Exception {
        // Initialize the database
        insertedOrderCustomer = orderCustomerRepository.saveAndFlush(orderCustomer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderCustomer
        restOrderCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderCustomer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderCustomerRepository.count();
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

    protected OrderCustomer getPersistedOrderCustomer(OrderCustomer orderCustomer) {
        return orderCustomerRepository.findById(orderCustomer.getId()).orElseThrow();
    }

    protected void assertPersistedOrderCustomerToMatchAllProperties(OrderCustomer expectedOrderCustomer) {
        assertOrderCustomerAllPropertiesEquals(expectedOrderCustomer, getPersistedOrderCustomer(expectedOrderCustomer));
    }

    protected void assertPersistedOrderCustomerToMatchUpdatableProperties(OrderCustomer expectedOrderCustomer) {
        assertOrderCustomerAllUpdatablePropertiesEquals(expectedOrderCustomer, getPersistedOrderCustomer(expectedOrderCustomer));
    }
}
