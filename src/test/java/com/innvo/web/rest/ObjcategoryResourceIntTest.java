package com.innvo.web.rest;

import com.innvo.Application;
import com.innvo.domain.Objcategory;
import com.innvo.repository.ObjcategoryRepository;
import com.innvo.repository.search.ObjcategorySearchRepository;

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
 * Test class for the ObjcategoryResource REST controller.
 *
 * @see ObjcategoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ObjcategoryResourceIntTest {

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
    private ObjcategoryRepository objcategoryRepository;

    @Inject
    private ObjcategorySearchRepository objcategorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restObjcategoryMockMvc;

    private Objcategory objcategory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ObjcategoryResource objcategoryResource = new ObjcategoryResource();
        ReflectionTestUtils.setField(objcategoryResource, "objcategoryRepository", objcategoryRepository);
        ReflectionTestUtils.setField(objcategoryResource, "objcategorySearchRepository", objcategorySearchRepository);
        this.restObjcategoryMockMvc = MockMvcBuilders.standaloneSetup(objcategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        objcategory = new Objcategory();
        objcategory.setName(DEFAULT_NAME);
        objcategory.setStatus(DEFAULT_STATUS);
        objcategory.setLastmodifiedby(DEFAULT_LASTMODIFIEDBY);
        objcategory.setLastmodifieddate(DEFAULT_LASTMODIFIEDDATE);
        objcategory.setDomain(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void createObjcategory() throws Exception {
        int databaseSizeBeforeCreate = objcategoryRepository.findAll().size();

        // Create the Objcategory

        restObjcategoryMockMvc.perform(post("/api/objcategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objcategory)))
                .andExpect(status().isCreated());

        // Validate the Objcategory in the database
        List<Objcategory> objcategorys = objcategoryRepository.findAll();
        assertThat(objcategorys).hasSize(databaseSizeBeforeCreate + 1);
        Objcategory testObjcategory = objcategorys.get(objcategorys.size() - 1);
        assertThat(testObjcategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testObjcategory.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testObjcategory.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testObjcategory.getLastmodifieddate()).isEqualTo(DEFAULT_LASTMODIFIEDDATE);
        assertThat(testObjcategory.getDomain()).isEqualTo(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = objcategoryRepository.findAll().size();
        // set the field null
        objcategory.setName(null);

        // Create the Objcategory, which fails.

        restObjcategoryMockMvc.perform(post("/api/objcategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objcategory)))
                .andExpect(status().isBadRequest());

        List<Objcategory> objcategorys = objcategoryRepository.findAll();
        assertThat(objcategorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = objcategoryRepository.findAll().size();
        // set the field null
        objcategory.setStatus(null);

        // Create the Objcategory, which fails.

        restObjcategoryMockMvc.perform(post("/api/objcategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objcategory)))
                .andExpect(status().isBadRequest());

        List<Objcategory> objcategorys = objcategoryRepository.findAll();
        assertThat(objcategorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastmodifieddateIsRequired() throws Exception {
        int databaseSizeBeforeTest = objcategoryRepository.findAll().size();
        // set the field null
        objcategory.setLastmodifieddate(null);

        // Create the Objcategory, which fails.

        restObjcategoryMockMvc.perform(post("/api/objcategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objcategory)))
                .andExpect(status().isBadRequest());

        List<Objcategory> objcategorys = objcategoryRepository.findAll();
        assertThat(objcategorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDomainIsRequired() throws Exception {
        int databaseSizeBeforeTest = objcategoryRepository.findAll().size();
        // set the field null
        objcategory.setDomain(null);

        // Create the Objcategory, which fails.

        restObjcategoryMockMvc.perform(post("/api/objcategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objcategory)))
                .andExpect(status().isBadRequest());

        List<Objcategory> objcategorys = objcategoryRepository.findAll();
        assertThat(objcategorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllObjcategorys() throws Exception {
        // Initialize the database
        objcategoryRepository.saveAndFlush(objcategory);

        // Get all the objcategorys
        restObjcategoryMockMvc.perform(get("/api/objcategorys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(objcategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
                .andExpect(jsonPath("$.[*].lastmodifieddate").value(hasItem(DEFAULT_LASTMODIFIEDDATE_STR)))
                .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())));
    }

    @Test
    @Transactional
    public void getObjcategory() throws Exception {
        // Initialize the database
        objcategoryRepository.saveAndFlush(objcategory);

        // Get the objcategory
        restObjcategoryMockMvc.perform(get("/api/objcategorys/{id}", objcategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(objcategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddate").value(DEFAULT_LASTMODIFIEDDATE_STR))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingObjcategory() throws Exception {
        // Get the objcategory
        restObjcategoryMockMvc.perform(get("/api/objcategorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateObjcategory() throws Exception {
        // Initialize the database
        objcategoryRepository.saveAndFlush(objcategory);

		int databaseSizeBeforeUpdate = objcategoryRepository.findAll().size();

        // Update the objcategory
        objcategory.setName(UPDATED_NAME);
        objcategory.setStatus(UPDATED_STATUS);
        objcategory.setLastmodifiedby(UPDATED_LASTMODIFIEDBY);
        objcategory.setLastmodifieddate(UPDATED_LASTMODIFIEDDATE);
        objcategory.setDomain(UPDATED_DOMAIN);

        restObjcategoryMockMvc.perform(put("/api/objcategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objcategory)))
                .andExpect(status().isOk());

        // Validate the Objcategory in the database
        List<Objcategory> objcategorys = objcategoryRepository.findAll();
        assertThat(objcategorys).hasSize(databaseSizeBeforeUpdate);
        Objcategory testObjcategory = objcategorys.get(objcategorys.size() - 1);
        assertThat(testObjcategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testObjcategory.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testObjcategory.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testObjcategory.getLastmodifieddate()).isEqualTo(UPDATED_LASTMODIFIEDDATE);
        assertThat(testObjcategory.getDomain()).isEqualTo(UPDATED_DOMAIN);
    }

    @Test
    @Transactional
    public void deleteObjcategory() throws Exception {
        // Initialize the database
        objcategoryRepository.saveAndFlush(objcategory);

		int databaseSizeBeforeDelete = objcategoryRepository.findAll().size();

        // Get the objcategory
        restObjcategoryMockMvc.perform(delete("/api/objcategorys/{id}", objcategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Objcategory> objcategorys = objcategoryRepository.findAll();
        assertThat(objcategorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
