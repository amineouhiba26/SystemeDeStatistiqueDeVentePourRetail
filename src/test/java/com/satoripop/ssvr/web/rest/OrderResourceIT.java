package com.satoripop.ssvr.web.rest;

import static com.satoripop.ssvr.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.satoripop.ssvr.IntegrationTest;
import com.satoripop.ssvr.domain.Order;
import com.satoripop.ssvr.repository.OrderRepository;
import com.satoripop.ssvr.service.dto.OrderDTO;
import com.satoripop.ssvr.service.mapper.OrderMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderResourceIT {

    private static final String DEFAULT_INCREMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_INCREMENT_ID = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ORDER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ORDER_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Double DEFAULT_GRAND_TOTAL = 1D;
    private static final Double UPDATED_GRAND_TOTAL = 2D;

    private static final Double DEFAULT_TOTAL_INVOICED = 1D;
    private static final Double UPDATED_TOTAL_INVOICED = 2D;

    private static final Double DEFAULT_TOTAL_DUE = 1D;
    private static final Double UPDATED_TOTAL_DUE = 2D;

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .incrementId(DEFAULT_INCREMENT_ID)
            .orderDate(DEFAULT_ORDER_DATE)
            .status(DEFAULT_STATUS)
            .grandTotal(DEFAULT_GRAND_TOTAL)
            .totalInvoiced(DEFAULT_TOTAL_INVOICED)
            .totalDue(DEFAULT_TOTAL_DUE);
        return order;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .incrementId(UPDATED_INCREMENT_ID)
            .orderDate(UPDATED_ORDER_DATE)
            .status(UPDATED_STATUS)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .totalInvoiced(UPDATED_TOTAL_INVOICED)
            .totalDue(UPDATED_TOTAL_DUE);
        return order;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();
        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);
        restOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getIncrementId()).isEqualTo(DEFAULT_INCREMENT_ID);
        assertThat(testOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrder.getGrandTotal()).isEqualTo(DEFAULT_GRAND_TOTAL);
        assertThat(testOrder.getTotalInvoiced()).isEqualTo(DEFAULT_TOTAL_INVOICED);
        assertThat(testOrder.getTotalDue()).isEqualTo(DEFAULT_TOTAL_DUE);
    }

    @Test
    @Transactional
    void createOrderWithExistingId() throws Exception {
        // Create the Order with an existing ID
        orderRepository.saveAndFlush(order);
        OrderDTO orderDTO = orderMapper.toDto(order);

        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIncrementIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setIncrementId(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        restOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setOrderDate(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        restOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setStatus(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        restOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGrandTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setGrandTotal(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        restOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().toString())))
            .andExpect(jsonPath("$.[*].incrementId").value(hasItem(DEFAULT_INCREMENT_ID)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(sameInstant(DEFAULT_ORDER_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].grandTotal").value(hasItem(DEFAULT_GRAND_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].totalInvoiced").value(hasItem(DEFAULT_TOTAL_INVOICED.doubleValue())))
            .andExpect(jsonPath("$.[*].totalDue").value(hasItem(DEFAULT_TOTAL_DUE.doubleValue())));
    }

    @Test
    @Transactional
    void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().toString()))
            .andExpect(jsonPath("$.incrementId").value(DEFAULT_INCREMENT_ID))
            .andExpect(jsonPath("$.orderDate").value(sameInstant(DEFAULT_ORDER_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.grandTotal").value(DEFAULT_GRAND_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.totalInvoiced").value(DEFAULT_TOTAL_INVOICED.doubleValue()))
            .andExpect(jsonPath("$.totalDue").value(DEFAULT_TOTAL_DUE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .incrementId(UPDATED_INCREMENT_ID)
            .orderDate(UPDATED_ORDER_DATE)
            .status(UPDATED_STATUS)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .totalInvoiced(UPDATED_TOTAL_INVOICED)
            .totalDue(UPDATED_TOTAL_DUE);
        OrderDTO orderDTO = orderMapper.toDto(updatedOrder);

        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getIncrementId()).isEqualTo(UPDATED_INCREMENT_ID);
        assertThat(testOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrder.getGrandTotal()).isEqualTo(UPDATED_GRAND_TOTAL);
        assertThat(testOrder.getTotalInvoiced()).isEqualTo(UPDATED_TOTAL_INVOICED);
        assertThat(testOrder.getTotalDue()).isEqualTo(UPDATED_TOTAL_DUE);
    }

    @Test
    @Transactional
    void putNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(UUID.randomUUID());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(UUID.randomUUID());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(UUID.randomUUID());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .incrementId(UPDATED_INCREMENT_ID)
            .status(UPDATED_STATUS)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .totalInvoiced(UPDATED_TOTAL_INVOICED)
            .totalDue(UPDATED_TOTAL_DUE);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getIncrementId()).isEqualTo(UPDATED_INCREMENT_ID);
        assertThat(testOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrder.getGrandTotal()).isEqualTo(UPDATED_GRAND_TOTAL);
        assertThat(testOrder.getTotalInvoiced()).isEqualTo(UPDATED_TOTAL_INVOICED);
        assertThat(testOrder.getTotalDue()).isEqualTo(UPDATED_TOTAL_DUE);
    }

    @Test
    @Transactional
    void fullUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .incrementId(UPDATED_INCREMENT_ID)
            .orderDate(UPDATED_ORDER_DATE)
            .status(UPDATED_STATUS)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .totalInvoiced(UPDATED_TOTAL_INVOICED)
            .totalDue(UPDATED_TOTAL_DUE);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getIncrementId()).isEqualTo(UPDATED_INCREMENT_ID);
        assertThat(testOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrder.getGrandTotal()).isEqualTo(UPDATED_GRAND_TOTAL);
        assertThat(testOrder.getTotalInvoiced()).isEqualTo(UPDATED_TOTAL_INVOICED);
        assertThat(testOrder.getTotalDue()).isEqualTo(UPDATED_TOTAL_DUE);
    }

    @Test
    @Transactional
    void patchNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(UUID.randomUUID());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(UUID.randomUUID());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(UUID.randomUUID());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, order.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
