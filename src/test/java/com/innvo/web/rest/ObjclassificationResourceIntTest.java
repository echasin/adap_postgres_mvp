package com.innvo.web.rest;

import com.innvo.Application;
import com.innvo.domain.Objclassification;
import com.innvo.repository.ObjclassificationRepository;
import com.innvo.repository.search.ObjclassificationSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.innvo.domain.enumeration.Status;

/**
 * Test class for the ObjclassificationResource REST controller.
 *
 * @see ObjclassificationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ObjclassificationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";


private static final Status DEFAULT_STATUS = Status.Active;
    private static final Status UPDATED_STATUS = Status.Pending;
    private static final String DEFAULT_LASTMODIFIEDBY = "AAAAA";
    private static final String UPDATED_LASTMODIFIEDBY = "BBBBB";

    private static final ZonedDateTime DEFAULT_LASTMODIFIEDDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LASTMODIFIEDDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LASTMODIFIEDDATE_STR = dateTimeFormatter.format(DEFAULT_LASTMODIFIEDDATE);
    private static final String DEFAULT_DOMAIN = "AAAAA";
    private static final String UPDATED_DOMAIN = "BBBBB";

    @Inject
    private ObjclassificationRepository objclassificationRepository;

    @Inject
    private ObjclassificationSearchRepository objclassificationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restObjclassificationMockMvc;

    private Objclassification objclassification;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ObjclassificationResource objclassificationResource = new ObjclassificationResource();
        ReflectionTestUtils.setField(objclassificationResource, "objclassificationRepository", objclassificationRepository);
        ReflectionTestUtils.setField(objclassificationResource, "objclassificationSearchRepository", objclassificationSearchRepository);
        this.restObjclassificationMockMvc = MockMvcBuilders.standaloneSetup(objclassificationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        objclassification = new Objclassification();
        objclassification.setName(DEFAULT_NAME);
        objclassification.setStatus(DEFAULT_STATUS);
        objclassification.setLastmodifiedby(DEFAULT_LASTMODIFIEDBY);
        objclassification.setLastmodifieddate(DEFAULT_LASTMODIFIEDDATE);
        objclassification.setDomain(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void createObjclassification() throws Exception {
        int databaseSizeBeforeCreate = objclassificationRepository.findAll().size();

        // Create the Objclassification

        restObjclassificationMockMvc.perform(post("/api/objclassifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objclassification)))
                .andExpect(status().isCreated());

        // Validate the Objclassification in the database
        List<Objclassification> objclassifications = objclassificationRepository.findAll();
        assertThat(objclassifications).hasSize(databaseSizeBeforeCreate + 1);
        Objclassification testObjclassification = objclassifications.get(objclassifications.size() - 1);
        assertThat(testObjclassification.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testObjclassification.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testObjclassification.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testObjclassification.getLastmodifieddate()).isEqualTo(DEFAULT_LASTMODIFIEDDATE);
        assertThat(testObjclassification.getDomain()).isEqualTo(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = objclassificationRepository.findAll().size();
        // set the field null
        objclassification.setName(null);

        // Create the Objclassification, which fails.

        restObjclassificationMockMvc.perform(post("/api/objclassifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objclassification)))
                .andExpect(status().isBadRequest());

        List<Objclassification> objclassifications = objclassificationRepository.findAll();
        assertThat(objclassifications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = objclassificationRepository.findAll().size();
        // set the field null
        objclassification.setStatus(null);

        // Create the Objclassification, which fails.

        restObjclassificationMockMvc.perform(post("/api/objclassifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objclassification)))
                .andExpect(status().isBadRequest());

        List<Objclassification> objclassifications = objclassificationRepository.findAll();
        assertThat(objclassifications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastmodifieddateIsRequired() throws Exception {
        int databaseSizeBeforeTest = objclassificationRepository.findAll().size();
        // set the field null
        objclassification.setLastmodifieddate(null);

        // Create the Objclassification, which fails.

        restObjclassificationMockMvc.perform(post("/api/objclassifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objclassification)))
                .andExpect(status().isBadRequest());

        List<Objclassification> objclassifications = objclassificationRepository.findAll();
        assertThat(objclassifications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDomainIsRequired() throws Exception {
        int databaseSizeBeforeTest = objclassificationRepository.findAll().size();
        // set the field null
        objclassification.setDomain(null);

        // Create the Objclassification, which fails.

        restObjclassificationMockMvc.perform(post("/api/objclassifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objclassification)))
                .andExpect(status().isBadRequest());

        List<Objclassification> objclassifications = objclassificationRepository.findAll();
        assertThat(objclassifications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllObjclassifications() throws Exception {
        // Initialize the database
        objclassificationRepository.saveAndFlush(objclassification);

        // Get all the objclassifications
        restObjclassificationMockMvc.perform(get("/api/objclassifications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(objclassification.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
                .andExpect(jsonPath("$.[*].lastmodifieddate").value(hasItem(DEFAULT_LASTMODIFIEDDATE_STR)))
                .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())));
    }

    @Test
    @Transactional
    public void getObjclassification() throws Exception {
        // Initialize the database
        objclassificationRepository.saveAndFlush(objclassification);

        // Get the objclassification
        restObjclassificationMockMvc.perform(get("/api/objclassifications/{id}", objclassification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(objclassification.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddate").value(DEFAULT_LASTMODIFIEDDATE_STR))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingObjclassification() throws Exception {
        // Get the objclassification
        restObjclassificationMockMvc.perform(get("/api/objclassifications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateObjclassification() throws Exception {
        // Initialize the database
        objclassificationRepository.saveAndFlush(objclassification);

		int databaseSizeBeforeUpdate = objclassificationRepository.findAll().size();

        // Update the objclassification
        objclassification.setName(UPDATED_NAME);
        objclassification.setStatus(UPDATED_STATUS);
        objclassification.setLastmodifiedby(UPDATED_LASTMODIFIEDBY);
        objclassification.setLastmodifieddate(UPDATED_LASTMODIFIEDDATE);
        objclassification.setDomain(UPDATED_DOMAIN);

        restObjclassificationMockMvc.perform(put("/api/objclassifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objclassification)))
                .andExpect(status().isOk());

        // Validate the Objclassification in the database
        List<Objclassification> objclassifications = objclassificationRepository.findAll();
        assertThat(objclassifications).hasSize(databaseSizeBeforeUpdate);
        Objclassification testObjclassification = objclassifications.get(objclassifications.size() - 1);
        assertThat(testObjclassification.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testObjclassification.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testObjclassification.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testObjclassification.getLastmodifieddate()).isEqualTo(UPDATED_LASTMODIFIEDDATE);
        assertThat(testObjclassification.getDomain()).isEqualTo(UPDATED_DOMAIN);
    }

    @Test
    @Transactional
    public void deleteObjclassification() throws Exception {
        // Initialize the database
        objclassificationRepository.saveAndFlush(objclassification);

		int databaseSizeBeforeDelete = objclassificationRepository.findAll().size();

        // Get the objclassification
        restObjclassificationMockMvc.perform(delete("/api/objclassifications/{id}", objclassification.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Objclassification> objclassifications = objclassificationRepository.findAll();
        assertThat(objclassifications).hasSize(databaseSizeBeforeDelete - 1);
    }
}
