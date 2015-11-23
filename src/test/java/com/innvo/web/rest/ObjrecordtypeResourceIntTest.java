package com.innvo.web.rest;

import com.innvo.Application;
import com.innvo.domain.Objrecordtype;
import com.innvo.repository.ObjrecordtypeRepository;
import com.innvo.repository.search.ObjrecordtypeSearchRepository;

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
 * Test class for the ObjrecordtypeResource REST controller.
 *
 * @see ObjrecordtypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ObjrecordtypeResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_OBJECTTYPE = "AAAAA";
    private static final String UPDATED_OBJECTTYPE = "BBBBB";
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
    private ObjrecordtypeRepository objrecordtypeRepository;

    @Inject
    private ObjrecordtypeSearchRepository objrecordtypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restObjrecordtypeMockMvc;

    private Objrecordtype objrecordtype;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ObjrecordtypeResource objrecordtypeResource = new ObjrecordtypeResource();
        ReflectionTestUtils.setField(objrecordtypeResource, "objrecordtypeRepository", objrecordtypeRepository);
        ReflectionTestUtils.setField(objrecordtypeResource, "objrecordtypeSearchRepository", objrecordtypeSearchRepository);
        this.restObjrecordtypeMockMvc = MockMvcBuilders.standaloneSetup(objrecordtypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        objrecordtype = new Objrecordtype();
        objrecordtype.setObjecttype(DEFAULT_OBJECTTYPE);
        objrecordtype.setName(DEFAULT_NAME);
        objrecordtype.setStatus(DEFAULT_STATUS);
        objrecordtype.setLastmodifiedby(DEFAULT_LASTMODIFIEDBY);
        objrecordtype.setLastmodifieddate(DEFAULT_LASTMODIFIEDDATE);
        objrecordtype.setDomain(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void createObjrecordtype() throws Exception {
        int databaseSizeBeforeCreate = objrecordtypeRepository.findAll().size();

        // Create the Objrecordtype

        restObjrecordtypeMockMvc.perform(post("/api/objrecordtypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objrecordtype)))
                .andExpect(status().isCreated());

        // Validate the Objrecordtype in the database
        List<Objrecordtype> objrecordtypes = objrecordtypeRepository.findAll();
        assertThat(objrecordtypes).hasSize(databaseSizeBeforeCreate + 1);
        Objrecordtype testObjrecordtype = objrecordtypes.get(objrecordtypes.size() - 1);
        assertThat(testObjrecordtype.getObjecttype()).isEqualTo(DEFAULT_OBJECTTYPE);
        assertThat(testObjrecordtype.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testObjrecordtype.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testObjrecordtype.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testObjrecordtype.getLastmodifieddate()).isEqualTo(DEFAULT_LASTMODIFIEDDATE);
        assertThat(testObjrecordtype.getDomain()).isEqualTo(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void checkObjecttypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = objrecordtypeRepository.findAll().size();
        // set the field null
        objrecordtype.setObjecttype(null);

        // Create the Objrecordtype, which fails.

        restObjrecordtypeMockMvc.perform(post("/api/objrecordtypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objrecordtype)))
                .andExpect(status().isBadRequest());

        List<Objrecordtype> objrecordtypes = objrecordtypeRepository.findAll();
        assertThat(objrecordtypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = objrecordtypeRepository.findAll().size();
        // set the field null
        objrecordtype.setName(null);

        // Create the Objrecordtype, which fails.

        restObjrecordtypeMockMvc.perform(post("/api/objrecordtypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objrecordtype)))
                .andExpect(status().isBadRequest());

        List<Objrecordtype> objrecordtypes = objrecordtypeRepository.findAll();
        assertThat(objrecordtypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = objrecordtypeRepository.findAll().size();
        // set the field null
        objrecordtype.setStatus(null);

        // Create the Objrecordtype, which fails.

        restObjrecordtypeMockMvc.perform(post("/api/objrecordtypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objrecordtype)))
                .andExpect(status().isBadRequest());

        List<Objrecordtype> objrecordtypes = objrecordtypeRepository.findAll();
        assertThat(objrecordtypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDomainIsRequired() throws Exception {
        int databaseSizeBeforeTest = objrecordtypeRepository.findAll().size();
        // set the field null
        objrecordtype.setDomain(null);

        // Create the Objrecordtype, which fails.

        restObjrecordtypeMockMvc.perform(post("/api/objrecordtypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objrecordtype)))
                .andExpect(status().isBadRequest());

        List<Objrecordtype> objrecordtypes = objrecordtypeRepository.findAll();
        assertThat(objrecordtypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllObjrecordtypes() throws Exception {
        // Initialize the database
        objrecordtypeRepository.saveAndFlush(objrecordtype);

        // Get all the objrecordtypes
        restObjrecordtypeMockMvc.perform(get("/api/objrecordtypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(objrecordtype.getId().intValue())))
                .andExpect(jsonPath("$.[*].objecttype").value(hasItem(DEFAULT_OBJECTTYPE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
                .andExpect(jsonPath("$.[*].lastmodifieddate").value(hasItem(DEFAULT_LASTMODIFIEDDATE_STR)))
                .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())));
    }

    @Test
    @Transactional
    public void getObjrecordtype() throws Exception {
        // Initialize the database
        objrecordtypeRepository.saveAndFlush(objrecordtype);

        // Get the objrecordtype
        restObjrecordtypeMockMvc.perform(get("/api/objrecordtypes/{id}", objrecordtype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(objrecordtype.getId().intValue()))
            .andExpect(jsonPath("$.objecttype").value(DEFAULT_OBJECTTYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddate").value(DEFAULT_LASTMODIFIEDDATE_STR))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingObjrecordtype() throws Exception {
        // Get the objrecordtype
        restObjrecordtypeMockMvc.perform(get("/api/objrecordtypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateObjrecordtype() throws Exception {
        // Initialize the database
        objrecordtypeRepository.saveAndFlush(objrecordtype);

		int databaseSizeBeforeUpdate = objrecordtypeRepository.findAll().size();

        // Update the objrecordtype
        objrecordtype.setObjecttype(UPDATED_OBJECTTYPE);
        objrecordtype.setName(UPDATED_NAME);
        objrecordtype.setStatus(UPDATED_STATUS);
        objrecordtype.setLastmodifiedby(UPDATED_LASTMODIFIEDBY);
        objrecordtype.setLastmodifieddate(UPDATED_LASTMODIFIEDDATE);
        objrecordtype.setDomain(UPDATED_DOMAIN);

        restObjrecordtypeMockMvc.perform(put("/api/objrecordtypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objrecordtype)))
                .andExpect(status().isOk());

        // Validate the Objrecordtype in the database
        List<Objrecordtype> objrecordtypes = objrecordtypeRepository.findAll();
        assertThat(objrecordtypes).hasSize(databaseSizeBeforeUpdate);
        Objrecordtype testObjrecordtype = objrecordtypes.get(objrecordtypes.size() - 1);
        assertThat(testObjrecordtype.getObjecttype()).isEqualTo(UPDATED_OBJECTTYPE);
        assertThat(testObjrecordtype.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testObjrecordtype.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testObjrecordtype.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testObjrecordtype.getLastmodifieddate()).isEqualTo(UPDATED_LASTMODIFIEDDATE);
        assertThat(testObjrecordtype.getDomain()).isEqualTo(UPDATED_DOMAIN);
    }

    @Test
    @Transactional
    public void deleteObjrecordtype() throws Exception {
        // Initialize the database
        objrecordtypeRepository.saveAndFlush(objrecordtype);

		int databaseSizeBeforeDelete = objrecordtypeRepository.findAll().size();

        // Get the objrecordtype
        restObjrecordtypeMockMvc.perform(delete("/api/objrecordtypes/{id}", objrecordtype.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Objrecordtype> objrecordtypes = objrecordtypeRepository.findAll();
        assertThat(objrecordtypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
