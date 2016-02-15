package com.innvo.web.rest;

import com.innvo.Application;
import com.innvo.domain.Scorefactor;
import com.innvo.repository.ScorefactorRepository;
import com.innvo.repository.search.ScorefactorSearchRepository;

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
 * Test class for the ScorefactorResource REST controller.
 *
 * @see ScorefactorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ScorefactorResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_MATCHATTRIBUTE = "AAAAA";
    private static final String UPDATED_MATCHATTRIBUTE = "BBBBB";
    private static final String DEFAULT_MATCHVALUE = "AAAAA";
    private static final String UPDATED_MATCHVALUE = "BBBBB";

    private static final Double DEFAULT_SCOREVALUE = 1D;
    private static final Double UPDATED_SCOREVALUE = 2D;
    private static final String DEFAULT_SCORETEXT = "AAAAA";
    private static final String UPDATED_SCORETEXT = "BBBBB";


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
    private ScorefactorRepository scorefactorRepository;

    @Inject
    private ScorefactorSearchRepository scorefactorSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restScorefactorMockMvc;

    private Scorefactor scorefactor;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScorefactorResource scorefactorResource = new ScorefactorResource();
        ReflectionTestUtils.setField(scorefactorResource, "scorefactorSearchRepository", scorefactorSearchRepository);
        ReflectionTestUtils.setField(scorefactorResource, "scorefactorRepository", scorefactorRepository);
        this.restScorefactorMockMvc = MockMvcBuilders.standaloneSetup(scorefactorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        scorefactor = new Scorefactor();
        scorefactor.setName(DEFAULT_NAME);
        scorefactor.setDescription(DEFAULT_DESCRIPTION);
        scorefactor.setMatchattribute(DEFAULT_MATCHATTRIBUTE);
        scorefactor.setMatchvalue(DEFAULT_MATCHVALUE);
        scorefactor.setScorevalue(DEFAULT_SCOREVALUE);
        scorefactor.setScoretext(DEFAULT_SCORETEXT);
        scorefactor.setStatus(DEFAULT_STATUS);
        scorefactor.setLastmodifiedby(DEFAULT_LASTMODIFIEDBY);
        scorefactor.setLastmodifieddate(DEFAULT_LASTMODIFIEDDATE);
        scorefactor.setDomain(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void createScorefactor() throws Exception {
        int databaseSizeBeforeCreate = scorefactorRepository.findAll().size();

        // Create the Scorefactor

        restScorefactorMockMvc.perform(post("/api/scorefactors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scorefactor)))
                .andExpect(status().isCreated());

        // Validate the Scorefactor in the database
        List<Scorefactor> scorefactors = scorefactorRepository.findAll();
        assertThat(scorefactors).hasSize(databaseSizeBeforeCreate + 1);
        Scorefactor testScorefactor = scorefactors.get(scorefactors.size() - 1);
        assertThat(testScorefactor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScorefactor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testScorefactor.getMatchattribute()).isEqualTo(DEFAULT_MATCHATTRIBUTE);
        assertThat(testScorefactor.getMatchvalue()).isEqualTo(DEFAULT_MATCHVALUE);
        assertThat(testScorefactor.getScorevalue()).isEqualTo(DEFAULT_SCOREVALUE);
        assertThat(testScorefactor.getScoretext()).isEqualTo(DEFAULT_SCORETEXT);
        assertThat(testScorefactor.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testScorefactor.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testScorefactor.getLastmodifieddate()).isEqualTo(DEFAULT_LASTMODIFIEDDATE);
        assertThat(testScorefactor.getDomain()).isEqualTo(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = scorefactorRepository.findAll().size();
        // set the field null
        scorefactor.setName(null);

        // Create the Scorefactor, which fails.

        restScorefactorMockMvc.perform(post("/api/scorefactors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scorefactor)))
                .andExpect(status().isBadRequest());

        List<Scorefactor> scorefactors = scorefactorRepository.findAll();
        assertThat(scorefactors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = scorefactorRepository.findAll().size();
        // set the field null
        scorefactor.setStatus(null);

        // Create the Scorefactor, which fails.

        restScorefactorMockMvc.perform(post("/api/scorefactors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scorefactor)))
                .andExpect(status().isBadRequest());

        List<Scorefactor> scorefactors = scorefactorRepository.findAll();
        assertThat(scorefactors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllScorefactors() throws Exception {
        // Initialize the database
        scorefactorRepository.saveAndFlush(scorefactor);

        // Get all the scorefactors
        restScorefactorMockMvc.perform(get("/api/scorefactors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(scorefactor.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].matchattribute").value(hasItem(DEFAULT_MATCHATTRIBUTE.toString())))
                .andExpect(jsonPath("$.[*].matchvalue").value(hasItem(DEFAULT_MATCHVALUE.toString())))
                .andExpect(jsonPath("$.[*].scorevalue").value(hasItem(DEFAULT_SCOREVALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].scoretext").value(hasItem(DEFAULT_SCORETEXT.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
                .andExpect(jsonPath("$.[*].lastmodifieddate").value(hasItem(DEFAULT_LASTMODIFIEDDATE_STR)))
                .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())));
    }

    @Test
    @Transactional
    public void getScorefactor() throws Exception {
        // Initialize the database
        scorefactorRepository.saveAndFlush(scorefactor);

        // Get the scorefactor
        restScorefactorMockMvc.perform(get("/api/scorefactors/{id}", scorefactor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(scorefactor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.matchattribute").value(DEFAULT_MATCHATTRIBUTE.toString()))
            .andExpect(jsonPath("$.matchvalue").value(DEFAULT_MATCHVALUE.toString()))
            .andExpect(jsonPath("$.scorevalue").value(DEFAULT_SCOREVALUE.doubleValue()))
            .andExpect(jsonPath("$.scoretext").value(DEFAULT_SCORETEXT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddate").value(DEFAULT_LASTMODIFIEDDATE_STR))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingScorefactor() throws Exception {
        // Get the scorefactor
        restScorefactorMockMvc.perform(get("/api/scorefactors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScorefactor() throws Exception {
        // Initialize the database
        scorefactorRepository.saveAndFlush(scorefactor);

		int databaseSizeBeforeUpdate = scorefactorRepository.findAll().size();

        // Update the scorefactor
        scorefactor.setName(UPDATED_NAME);
        scorefactor.setDescription(UPDATED_DESCRIPTION);
        scorefactor.setMatchattribute(UPDATED_MATCHATTRIBUTE);
        scorefactor.setMatchvalue(UPDATED_MATCHVALUE);
        scorefactor.setScorevalue(UPDATED_SCOREVALUE);
        scorefactor.setScoretext(UPDATED_SCORETEXT);
        scorefactor.setStatus(UPDATED_STATUS);
        scorefactor.setLastmodifiedby(UPDATED_LASTMODIFIEDBY);
        scorefactor.setLastmodifieddate(UPDATED_LASTMODIFIEDDATE);
        scorefactor.setDomain(UPDATED_DOMAIN);

        restScorefactorMockMvc.perform(put("/api/scorefactors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scorefactor)))
                .andExpect(status().isOk());

        // Validate the Scorefactor in the database
        List<Scorefactor> scorefactors = scorefactorRepository.findAll();
        assertThat(scorefactors).hasSize(databaseSizeBeforeUpdate);
        Scorefactor testScorefactor = scorefactors.get(scorefactors.size() - 1);
        assertThat(testScorefactor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScorefactor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testScorefactor.getMatchattribute()).isEqualTo(UPDATED_MATCHATTRIBUTE);
        assertThat(testScorefactor.getMatchvalue()).isEqualTo(UPDATED_MATCHVALUE);
        assertThat(testScorefactor.getScorevalue()).isEqualTo(UPDATED_SCOREVALUE);
        assertThat(testScorefactor.getScoretext()).isEqualTo(UPDATED_SCORETEXT);
        assertThat(testScorefactor.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testScorefactor.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testScorefactor.getLastmodifieddate()).isEqualTo(UPDATED_LASTMODIFIEDDATE);
        assertThat(testScorefactor.getDomain()).isEqualTo(UPDATED_DOMAIN);
    }

    @Test
    @Transactional
    public void deleteScorefactor() throws Exception {
        // Initialize the database
        scorefactorRepository.saveAndFlush(scorefactor);

		int databaseSizeBeforeDelete = scorefactorRepository.findAll().size();

        // Get the scorefactor
        restScorefactorMockMvc.perform(delete("/api/scorefactors/{id}", scorefactor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Scorefactor> scorefactors = scorefactorRepository.findAll();
        assertThat(scorefactors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
