package com.innvo.web.rest;

import com.innvo.Application;
import com.innvo.domain.Objtype;
import com.innvo.repository.ObjtypeRepository;
import com.innvo.repository.search.ObjtypeSearchRepository;

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
 * Test class for the ObjtypeResource REST controller.
 *
 * @see ObjtypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ObjtypeResourceIntTest {

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
    private ObjtypeRepository objtypeRepository;

    @Inject
    private ObjtypeSearchRepository objtypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restObjtypeMockMvc;

    private Objtype objtype;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ObjtypeResource objtypeResource = new ObjtypeResource();
        ReflectionTestUtils.setField(objtypeResource, "objtypeRepository", objtypeRepository);
        ReflectionTestUtils.setField(objtypeResource, "objtypeSearchRepository", objtypeSearchRepository);
        this.restObjtypeMockMvc = MockMvcBuilders.standaloneSetup(objtypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        objtype = new Objtype();
        objtype.setName(DEFAULT_NAME);
        objtype.setStatus(DEFAULT_STATUS);
        objtype.setLastmodifiedby(DEFAULT_LASTMODIFIEDBY);
        objtype.setLastmodifieddate(DEFAULT_LASTMODIFIEDDATE);
        objtype.setDomain(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void createObjtype() throws Exception {
        int databaseSizeBeforeCreate = objtypeRepository.findAll().size();

        // Create the Objtype

        restObjtypeMockMvc.perform(post("/api/objtypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objtype)))
                .andExpect(status().isCreated());

        // Validate the Objtype in the database
        List<Objtype> objtypes = objtypeRepository.findAll();
        assertThat(objtypes).hasSize(databaseSizeBeforeCreate + 1);
        Objtype testObjtype = objtypes.get(objtypes.size() - 1);
        assertThat(testObjtype.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testObjtype.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testObjtype.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testObjtype.getLastmodifieddate()).isEqualTo(DEFAULT_LASTMODIFIEDDATE);
        assertThat(testObjtype.getDomain()).isEqualTo(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = objtypeRepository.findAll().size();
        // set the field null
        objtype.setName(null);

        // Create the Objtype, which fails.

        restObjtypeMockMvc.perform(post("/api/objtypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objtype)))
                .andExpect(status().isBadRequest());

        List<Objtype> objtypes = objtypeRepository.findAll();
        assertThat(objtypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = objtypeRepository.findAll().size();
        // set the field null
        objtype.setStatus(null);

        // Create the Objtype, which fails.

        restObjtypeMockMvc.perform(post("/api/objtypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objtype)))
                .andExpect(status().isBadRequest());

        List<Objtype> objtypes = objtypeRepository.findAll();
        assertThat(objtypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastmodifieddateIsRequired() throws Exception {
        int databaseSizeBeforeTest = objtypeRepository.findAll().size();
        // set the field null
        objtype.setLastmodifieddate(null);

        // Create the Objtype, which fails.

        restObjtypeMockMvc.perform(post("/api/objtypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objtype)))
                .andExpect(status().isBadRequest());

        List<Objtype> objtypes = objtypeRepository.findAll();
        assertThat(objtypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDomainIsRequired() throws Exception {
        int databaseSizeBeforeTest = objtypeRepository.findAll().size();
        // set the field null
        objtype.setDomain(null);

        // Create the Objtype, which fails.

        restObjtypeMockMvc.perform(post("/api/objtypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objtype)))
                .andExpect(status().isBadRequest());

        List<Objtype> objtypes = objtypeRepository.findAll();
        assertThat(objtypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllObjtypes() throws Exception {
        // Initialize the database
        objtypeRepository.saveAndFlush(objtype);

        // Get all the objtypes
        restObjtypeMockMvc.perform(get("/api/objtypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(objtype.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
                .andExpect(jsonPath("$.[*].lastmodifieddate").value(hasItem(DEFAULT_LASTMODIFIEDDATE_STR)))
                .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())));
    }

    @Test
    @Transactional
    public void getObjtype() throws Exception {
        // Initialize the database
        objtypeRepository.saveAndFlush(objtype);

        // Get the objtype
        restObjtypeMockMvc.perform(get("/api/objtypes/{id}", objtype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(objtype.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddate").value(DEFAULT_LASTMODIFIEDDATE_STR))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingObjtype() throws Exception {
        // Get the objtype
        restObjtypeMockMvc.perform(get("/api/objtypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateObjtype() throws Exception {
        // Initialize the database
        objtypeRepository.saveAndFlush(objtype);

		int databaseSizeBeforeUpdate = objtypeRepository.findAll().size();

        // Update the objtype
        objtype.setName(UPDATED_NAME);
        objtype.setStatus(UPDATED_STATUS);
        objtype.setLastmodifiedby(UPDATED_LASTMODIFIEDBY);
        objtype.setLastmodifieddate(UPDATED_LASTMODIFIEDDATE);
        objtype.setDomain(UPDATED_DOMAIN);

        restObjtypeMockMvc.perform(put("/api/objtypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objtype)))
                .andExpect(status().isOk());

        // Validate the Objtype in the database
        List<Objtype> objtypes = objtypeRepository.findAll();
        assertThat(objtypes).hasSize(databaseSizeBeforeUpdate);
        Objtype testObjtype = objtypes.get(objtypes.size() - 1);
        assertThat(testObjtype.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testObjtype.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testObjtype.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testObjtype.getLastmodifieddate()).isEqualTo(UPDATED_LASTMODIFIEDDATE);
        assertThat(testObjtype.getDomain()).isEqualTo(UPDATED_DOMAIN);
    }

    @Test
    @Transactional
    public void deleteObjtype() throws Exception {
        // Initialize the database
        objtypeRepository.saveAndFlush(objtype);

		int databaseSizeBeforeDelete = objtypeRepository.findAll().size();

        // Get the objtype
        restObjtypeMockMvc.perform(delete("/api/objtypes/{id}", objtype.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Objtype> objtypes = objtypeRepository.findAll();
        assertThat(objtypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
